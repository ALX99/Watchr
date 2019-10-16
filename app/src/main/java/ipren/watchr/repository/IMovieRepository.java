package ipren.watchr.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

public interface IMovieRepository {
    final String TRENDING_LIST = "Trending";
    LiveData<Movie> getMovieByID(int id);

    LiveData<List<Genre>> getGenresFromMovie(int id);

    LiveData<List<Movie>> getMovieList(String listID, int page, boolean forceFetch);

    LiveData<List<Actor>> getActorsFromMovie(int movieID);
}

