package ipren.watchr.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.Constants;
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

    public MovieRepository(Context context) {
        movieDB = MovieDB.getInstance(context);
        movieApi = new MovieApi();
    }


    public LiveData<List<Genre>> getGenresFromMovie(int id) {
        return movieDB.movieGenreJoinDao().getGenresFromMovie(id);
    }

    public LiveData<List<Movie>> getTrending() {
        Call<MovieList> call = movieApi.getTrending();
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (!response.isSuccessful())
                    return;

                List<Movie> movies = response.body().getMovies();

                new Thread(() -> {
                    movieDB.movieDao().insert(movies);
                    for (Movie m : movies)
                        movieDB.movieListDao().insert(new MovieList(m.id, 1));
                }).start();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
        return movieDB.movieListDao().getMoviesFromList(1);
    }

    public LiveData<Movie> getMovieByID(int movieID) {
        Call<Movie> movieCall = movieApi.getMovie(movieID);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful())
                    return;

                Movie movie = response.body();
                // Insert stuff to db :)
                new Thread(() -> {
                    movieDB.movieDao().insert(movie);
                    movieDB.genreDao().insert(movie.getGenres());
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

        return movieDB.movieDao().getMovieByID(movieID);
    }

    public LiveData<List<Actor>> getActorsFromMovie(int movieID) {
        return movieDB.actorDao().getActorsFromMovie(movieID);
    }

}
