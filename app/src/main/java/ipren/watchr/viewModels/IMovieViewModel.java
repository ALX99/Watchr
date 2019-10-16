package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;

/**
 * The interface Movie view model.
 */
public interface IMovieViewModel {
    /**
     * Gets movie.
     *
     * @return the movie
     */
    LiveData<Movie> getMovie();

    /**
     * Sets movie id.
     *
     * @param movieID The movie ID
     */
    void setMovieID(int movieID);

    /**
     * Comment on movie.
     *
     * @param movieID The movie ID
     * @param UID     The user UID
     * @param text    The comment text
     */
    void commentOnMovie(int movieID, String UID, String text);


    /**
     * Gets user.
     *
     * @return Returns the logged in user
     */
    LiveData<User> getUser();


    /**
     * Gets comments.
     *
     * @return Returns the comments for the currently loaded movie
     */
    LiveData<FireComment[]> getComments();

    /**
     * Gets genres.
     *
     * @return the genres
     */
    LiveData<List<Genre>> getGenres();

    /**
     * Gets actors.
     *
     * @return the actors
     */
    LiveData<List<Actor>> getActors();

    /**
     * Add movie to list.
     *
     * @param movieID the movie id
     * @param list    the list
     * @param UID     the uid
     */
    void addMovieToList(int movieID, String list, String UID);

    /**
     * Remove movie from list.
     *
     * @param movieID the movie id
     * @param list    the list
     * @param UID     the uid
     */
    void removeMovieFromList(int movieID, String list, String UID);

    /**
     * Gets a specified user list.
     *
     * @param list the list to get
     * @param UID  the uid of the user
     * @return the user list
     */
    LiveData<String[]> getUserList(String list, String UID);

    /**
     * Gets ratings for the movie.
     *
     * @param searchMethod the search method
     * @return the ratings
     */
    LiveData<FireRating[]> getRatings(int searchMethod);

    /**
     * Gets public profile for a user.
     *
     * @param user_id the user UID
     * @return the public profile
     */
    LiveData<PublicProfile> getPublicProfile(String user_id);

}
