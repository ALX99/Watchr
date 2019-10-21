package ipren.watchr.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

/**
 * The interface Movie repository.
 */
public interface IMovieRepository {
    String TRENDING_LIST = "Trending";
    String DISCOVER_LIST = "Discover";
    String BROWSE_LIST = "Browse";
    String RECOMMENDED_LIST = "Recommended";

    /**
     * Gets a movie by its ID.
     *
     * @param ID The movie ID
     * @return The movie
     */
    LiveData<Movie> getMovieByID(int ID);

    /**
     * Gets a list of movies by there IDs
     *
     * @param IDs the IDs
     * @return the movie list
     */
    // will probably have to be used when wanting
    // to gather movies from a list from FireBase
    // since they only contain movieIDs
    LiveData<List<Movie>> getMoviesByID(int[] IDs);

    /**
     * Gets genres from movie.
     *
     * @param ID The ID for the movie
     * @return The list of genres for the movie
     */
    LiveData<List<Genre>> getGenresFromMovie(int ID);

    /**
     * Gets movie list.
     *
     * @param listID     The list ID
     * @param page       The page
     * @param forceFetch Should data be forced to be gathered from the API?
     * @return The list of movies
     */
    LiveData<List<Movie>> getMovieList(String listID, int page, boolean forceFetch);

    /**
     * Gets actors from movie.
     *
     * @param movieID The movie ID
     * @return The list of actors from the movie
     */
    LiveData<List<Actor>> getActorsFromMovie(int movieID);

    LiveData<List<Movie>> getDiscoverList(int[] genres, int page, boolean forceFetch);

}

