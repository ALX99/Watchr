package ipren.watchr.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataholders.Actor;
import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.Genre;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;

/*
The ViewModel only holds information and does not do anything else
besides calling other methods of other object to get relevant information.
It probably would be the equivalent of controller in MVC, as dumb as possible.
Why LiveData information is stored here is because fragments regularly gets killed
and therefore all information they hold, this is a way to temporarily save the information
and restore it quickly when the fragment is created again.
 */
public class MovieViewModel extends AndroidViewModel implements IMovieViewModel {
    private String movieID;
    private IMovieRepository movieRepository;
    private IUserDataRepository userDataRepository;
    private LiveData<Movie> movie;
    private LiveData<User> user;
    private LiveData<Comment[]> comments;
    private LiveData<List<Actor>> actors;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = IMovieRepository.getInstance();
        userDataRepository = IUserDataRepository.getInstance();
    }

    public void setMovieID(int movieID) {
        this.movieID = Integer.toString(movieID);
        this.movie = movieRepository.getMovieByID(movieID);
        this.comments = userDataRepository.getComments(Integer.toString(movieID), IUserDataRepository.SEARCH_METHOD_MOVIE_ID);
        this.user = userDataRepository.getUserLiveData();
        this.actors = movieRepository.getActorsFromMovie(movieID);
    }

    @Override
    public void addMovieToList(String list, String UID) {
        userDataRepository.addMovieToList(list, movieID, UID, null);
    }

    @Override
    public void removeMovieFromList(String list, String UID) {
        userDataRepository.removeMovieFromList(list, movieID, UID, null);
    }

    @Override
    public LiveData<Rating[]> getRatings() {
        return userDataRepository.getRatings(movieID, IUserDataRepository.SEARCH_METHOD_MOVIE_ID);
    }

    public void rateMovie(int score, String UID) {
        userDataRepository.rateMovie(score, movieID, UID, null);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    @Override
    public LiveData<List<Actor>> getActors() {
        return actors;
    }

    @Override
    public LiveData<String[]> getUserList(String list, String UID) {
        return userDataRepository.getMovieList(list, UID);
    }

    @Override
    public LiveData<User> getUser() {
        return user;
    }

    @Override
    public void commentOnMovie(String UID, String text) {
        userDataRepository.commentMovie(text, movieID, UID, null);
    }

    @Override
    public LiveData<Comment[]> getComments() {
        return comments;
    }

    @Override
    public LiveData<List<Genre>> getGenres() {
        return movieRepository.getGenresFromMovie(Integer.valueOf(movieID));
    }

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return userDataRepository.getPublicProfile(user_id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
