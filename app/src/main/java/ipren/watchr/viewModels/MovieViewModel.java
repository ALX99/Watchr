package ipren.watchr.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.MovieRepository;

public class MovieViewModel extends AndroidViewModel implements IMovieViewModel {
    private int movieID;
    private IMovieRepository movieRepository;
    private IUserDataRepository mainRepository;
    private LiveData<User> user;
    private LiveData<FireComment[]> comments;
    private LiveData<List<Actor>> actors;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application.getApplicationContext());
        mainRepository = IUserDataRepository.getInstance();
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
        this.comments = mainRepository.getComments(Integer.toString(movieID), IUserDataRepository.SEARCH_METHOD_MOVIE_ID);
        this.user = mainRepository.getUserLiveData();
        this.actors = movieRepository.getActorsFromMovie(movieID);
    }

    @Override
    public void addMovieToList(int movieID, String list, String UID) {
        mainRepository.addMovieToList(list, Integer.toString(movieID), UID, null);
    }

    @Override
    public void removeMovieFromList(int movieID, String list, String UID) {
        mainRepository.removeMovieFromList(list, Integer.toString(movieID), UID, null);
    }

    @Override
    public LiveData<FireRating[]> getRatings(int movie_id, int searchMethod) {
        return mainRepository.getRatings(Integer.toString(movie_id), searchMethod);
    }

    public LiveData<Movie> getMovie() {
        return movieRepository.getMovieByID(movieID);
    }

    @Override
    public LiveData<List<Actor>> getActors() {
        return actors;
    }

    @Override
    public LiveData<String[]> getUserList(String list, String UID) {
        return mainRepository.getMovieList(list, UID);
    }

    public LiveData<User> getUser() {
        return user;
    }

    @Override
    public void commentOnMovie(int movieID, String UID, String text) {
        mainRepository.commentMovie(text, Integer.toString(movieID), UID, null);
    }

    @Override
    public LiveData<FireComment[]> getComments() {
        return comments;
    }

    public LiveData<List<Genre>> getGenres() {
        return movieRepository.getGenresFromMovie(movieID);
    }

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return mainRepository.getPublicProfile(user_id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
