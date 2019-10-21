package ipren.watchr.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieGenreJoin;
import ipren.watchr.dataHolders.MovieList;
import ipren.watchr.repository.API.IMovieApi;
import ipren.watchr.repository.API.MovieApi;
import ipren.watchr.repository.Database.MovieDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieRepository implements IMovieRepository {
    private static final int INSERT = 1;
    private static final int UPDATE = 2;
    private MovieDB movieDB;
    private IMovieApi movieApi;

    /**
     * Instantiates a new Movie repository.
     *
     * @param context the context
     */
    public MovieRepository(Context context) {
        movieDB = MovieDB.getInstance(context);
        movieApi = new MovieApi();
    }


    public LiveData<List<Genre>> getGenresFromMovie(int id) {
        return movieDB.movieGenreJoinDao().getGenresFromMovie(id);
    }

    /**
     * @param list       The list to be returned
     * @param page       The page in the list
     * @param forceFetch Should data be forced to be gathered from the API?
     * @return The movie list
     */
    public LiveData<List<Movie>> getMovieList(String list, int page, boolean forceFetch) {
        if (list == IMovieRepository.TRENDING_LIST)
            getList(list, page, forceFetch, movieApi.getTrending(page));
        return movieDB.movieListDao().getMoviesFromList(list, page);
    }


    /**
     * Need another function for getting the discoverList
     * since there can be many different discoverLists
     * @param genres List of genres to include
     * @param page Page num
     * @param forceFetch Force daa from api?
     * @return The movie list
     */
    public LiveData<List<Movie>> getDiscoverList(int[] genres, int page, boolean forceFetch) {
        Arrays.sort(genres);
        String list = IMovieRepository.DISCOVER_LIST + genres.toString();
        getList(list, page, forceFetch, movieApi.getDiscover(genres, page));
        return movieDB.movieListDao().getMoviesFromList(list, page);
    }

    /**
     * @param page       The page number of the trending list
     * @param forceFetch Force fetching from the API?
     */
    private void getList(String list, int page, boolean forceFetch, Call<MovieList> call) {
        new Thread(() -> {
            List<Movie> movies = movieDB.movieListDao().getMoviesFromListNonLiveData(list, page);
            if (movies == null || movies.size() == 0 || movies.get(0).getUpdateDate() == null || forceFetch)
                insertMovieList(call, list, page);
            else {
                long diff = new Date().getTime() - movies.get(0).getUpdateDate().getTime();
                if (TimeUnit.MILLISECONDS.toDays(diff) > 1) {
                    Log.d("MOVIE", "The movie list " + list + " has to be updated!");
                    insertMovieList(call, list, page);
                } else
                    Log.d("MOVIE", "The movie list " + list + " does not have to be updated. It is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes old");
            }
        }).start();
    }

    /**
     * Handles inserting movies into the DB
     * @param call The API call
     * @param list The list to be inserted into
     * @param page The page
     */
    private void insertMovieList(Call<MovieList> call, String list, int page) {
        Log.d("MOVIE", "Fetching list from API");
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (!response.isSuccessful())
                    return;
                Date d = new Date();
                List<Movie> movies = response.body().getMovies();
                new Thread(() -> {
                    // We don't update the movie entries here
                    for (Movie m : movies) {
                        // If the movie doesn't exist in the DB already we have to insert it
                        if (movieDB.movieDao().getMovieByIDNonLiveObject(m.getId()) == null)
                            movieDB.movieDao().insert(m);
                        // Associate the movie with the movieList
                        movieDB.movieListDao().insert(new MovieList(m.id, list, page, d));
                    }
                }).start();
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }

    /**
     * @param ids The array of IDs of movies to get
     * @return The movie list
     */
    public LiveData<List<Movie>> getMoviesByID(int[] ids) {
        // We have to fetch the movies if they don't already exist
        for (int id : ids)
            getMovieByID(id);
        return movieDB.movieDao().getMoviesByID(ids);
    }

    /**
     *  Gets a movie from either the API or DB
     *  depending if it exists in the DB
     *  and how old it is
     * @param movieID The ID of the movie
     * @return The movie
     */
    public LiveData<Movie> getMovieByID(int movieID) {
        new Thread(() -> {
            Movie m = movieDB.movieDao().getMovieByIDNonLiveObject(movieID);
            if (m == null)
                insertMovie(movieID, INSERT);
                // If this condition is true, it means that the movie has been inserted from
                // the gathering of a movieList and does not contain the full information
                // therefore has to be updated
            else if (m != null && (m.getUpdateDate() == null || m.getActorList() == null))
                insertMovie(movieID, UPDATE);
            else {
                long diff = new Date().getTime() - m.getUpdateDate().getTime();
                if (TimeUnit.MILLISECONDS.toDays(diff) > 7) {
                    Log.d("MOVIE", "The movie " + movieID + " has to be updated!");
                    insertMovie(movieID, UPDATE);
                } else
                    Log.d("MOVIE", "The movie " + movieID + " does not have to be updated. It is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes old");
            }
        }).start();

        return movieDB.movieDao().getMovieByID(movieID);
    }

    /**
     * Insert a movie into the DB
     * @param movieID The ID of the movie
     * @param method  The method to be used, (inserting or updating)
     */
    private void insertMovie(int movieID, int method) {
        Log.d("MOVIE", "Fetching the movie " + movieID);
        Call<Movie> movieCall = movieApi.getMovie(movieID);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful())
                    return;
                Movie movie = response.body();
                // Insert stuff to db :)
                new Thread(() -> {
                    movie.setUpdateDate(new Date());
                    // The reason why this is needed has to do with LiveData lists.
                    // If a movie is inserted and a LiveData list containing that movie is present
                    // the list will remove the movie from the list for a brief period of time
                    // and then added back due to the movie being inserted already exists
                    // in the db and is essentially replaced which causes something that looks like a visual glitch.
                    // If we use update, the LiveData list containing the movie will update it instead of remove it
                    // and re-adding it, result being that it looks normal.
                    if (method == INSERT)
                        movieDB.movieDao().insert(movie);
                    else if (method == UPDATE)
                        movieDB.movieDao().update(movie);

                    for (Genre g : movie.getGenres())
                        movieDB.movieGenreJoinDao().insert(new MovieGenreJoin(movie.id, g.getGenreID()));
                    for (Actor a : movie.getActorList().getActors())
                        if (a.getPictureLink() != null)
                            movieDB.actorDao().insert(new Actor(movie.id, a));
                }).start();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    /**
     * @param movieID The movie ID
     * @return The list of actors from the movie
     */
    public LiveData<List<Actor>> getActorsFromMovie(int movieID) {
        return movieDB.actorDao().getActorsFromMovie(movieID);
    }

}
