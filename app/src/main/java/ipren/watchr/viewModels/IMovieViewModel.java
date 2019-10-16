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

public interface IMovieViewModel {
    LiveData<Movie> getMovie();

    void setMovieID(int movieID);

    void commentOnMovie(int movieID, String UID, String text);

    LiveData<User> getUser();

    LiveData<FireComment[]> getComments();

    LiveData<List<Genre>> getGenres();

    LiveData<List<Actor>> getActors();

    void addMovieToList(int movieID, String list, String UID);

    void removeMovieFromList(int movieID, String list, String UID);

    LiveData<String[]> getUserList(String list, String UID);

    LiveData<FireRating[]> getRatings(int movie_id, int searchMethod);

    LiveData<PublicProfile> getPublicProfile(String user_id);

}
