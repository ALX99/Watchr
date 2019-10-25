package ipren.watchr.viewmodels;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataholders.Actor;
import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.Genre;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;

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
     * @param UID  The user UID
     * @param text The comment text
     */
    void commentOnMovie(String UID, String text);


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
    LiveData<Comment[]> getComments();

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
     * @param list the list
     * @param UID  the uid
     */
    void addMovieToList(String list, String UID);

    /**
     * Remove movie from list.
     *
     * @param list the list
     * @param UID  the uid
     */
    void removeMovieFromList(String list, String UID);

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
     * @return the ratings
     */
    LiveData<Rating[]> getRatings();

    /**
     * Gets public profile for a user.
     *
     * @param user_id the user UID
     * @return the public profile
     */
    LiveData<PublicProfile> getPublicProfile(String user_id);

    /**
     * @param score User score
     * @param UID   the uid
     */
    void rateMovie(int score, String UID);


}
