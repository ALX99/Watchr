package ipren.watchr.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

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
    private MovieDB movieDB;
    private IMovieApi movieApi;
    private static final int INSERT = 1;
    private static final int UPDATE = 2;

    public MovieRepository(Context context) {
        movieDB = MovieDB.getInstance(context);
        movieApi = new MovieApi();
    }


    public LiveData<List<Genre>> getGenresFromMovie(int id) {
        return movieDB.movieGenreJoinDao().getGenresFromMovie(id);
    }

    public LiveData<List<Movie>> getMovieList(String list, int page, boolean forceFetch) {
        if (list == IMovieRepository.TRENDING_LIST)
            getTrendingList(page, forceFetch);
        return movieDB.movieListDao().getMoviesFromList(list, page);
    }

    private void getTrendingList(int page, boolean forceFetch) {
        new Thread(() -> {
            List<Movie> movies = movieDB.movieListDao().getMoviesFromListNonLiveData(IMovieRepository.TRENDING_LIST, page);
            if (movies == null || movies.size() == 0 || forceFetch)
                insertTrendingMovies(page);
            else {
                long diff = new Date().getTime() - movies.get(0).getUpdateDate().getTime();
                if (TimeUnit.MILLISECONDS.toDays(diff) > 1) {
                    Log.d("MOVIE", "The movie list " + IMovieRepository.TRENDING_LIST + " has to be updated!");
                    insertTrendingMovies(page);
                } else
                    Log.d("MOVIE", "The movie list " + IMovieRepository.TRENDING_LIST + " does not have to be updated. It is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes old");
            }
        }).start();
    }

    private void insertTrendingMovies(int page) {
        Log.d("MOVIE", "Fetching trending movies");
        Call<MovieList> call = movieApi.getTrending(page);
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
                        movieDB.movieListDao().insert(new MovieList(m.id, IMovieRepository.TRENDING_LIST, page, d));
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }

    public LiveData<List<Movie>> getMoviesByID(int[] ids) {
        // We have to fetch the movies if they don't already exist
        for (int id : ids)
            getMovieByID(id);
        return movieDB.movieDao().getMoviesByID(ids);
    }

    public LiveData<Movie> getMovieByID(int movieID) {
        new Thread(() -> {
            Movie m = movieDB.movieDao().getMovieByIDNonLiveObject(movieID);
            if (m == null)
                insertMovie(movieID, INSERT);
                // If this condition is true, it means that the movie has been inserted from
                // the gathering of a movieList and does not contain the full information
                // therefore has to be updated
            else if (m != null && m.getUpdateDate() == null)
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

    public LiveData<List<Actor>> getActorsFromMovie(int movieID) {
        return movieDB.actorDao().getActorsFromMovie(movieID);
    }

}
