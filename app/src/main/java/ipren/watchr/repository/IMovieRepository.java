package ipren.watchr.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

public interface IMovieRepository {

    LiveData<Movie> getMovieByID(int id);

    LiveData<List<Genre>> getGenresFromMovie(int id);

    LiveData<List<Movie>> getTrending();
}

