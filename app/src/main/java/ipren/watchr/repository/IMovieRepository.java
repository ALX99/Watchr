package ipren.watchr.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

public interface IMovieRepository {
    String TRENDING_LIST = "Trending";
    LiveData<Movie> getMovieByID(int id);

    // will probably have to be used when wanting
    // to gather movies from a list from FireBase
    // since they only contain movieIDs
    LiveData<List<Movie>> getMoviesByID(int[] ids);

    LiveData<List<Genre>> getGenresFromMovie(int id);

    LiveData<List<Movie>> getMovieList(String listID, int page, boolean forceFetch);

    LiveData<List<Actor>> getActorsFromMovie(int movieID);
}

