package ipren.watchr.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.Database.MovieDB;

public class MovieRepository implements IMovieRepository {
    private MovieDB movieDB;

    public MovieRepository(Context context) {
        movieDB = MovieDB.getInstance(context);
    }

    public LiveData<Movie> getMovieByID(int id) {
        return movieDB.movieDao().getMovieByID(id);
    }

    public LiveData<List<Genre>> getGenresFromMovie(int id) {
        return movieDB.movieGenreJoinDao().getGenresFromMovie(id);
    }
}
