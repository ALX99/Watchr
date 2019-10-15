package ipren.watchr.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMainRepository;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.MainRepository;
import ipren.watchr.repository.MovieRepository;

public class MovieViewModel extends AndroidViewModel implements IMovieViewModel {
    private int movieID;
    private IMovieRepository movieRepository;
    private IMainRepository mainRepository;
    private LiveData<Movie> movie;
    private LiveData<User> user;
    private LiveData<FireComment[]> comments;
    private LiveData<List<Actor>> actors;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application.getApplicationContext());
        mainRepository = IMainRepository.getMainRepository();
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
        this.movie = movieRepository.getMovieByID(movieID);
        this.comments = mainRepository.getComments(Integer.toString(movieID), MainRepository.SEARCH_METHOD_MOVIE_ID);
        this.user = mainRepository.getUserLiveData();
        this.actors = movieRepository.getActorsFromMovie(movieID);
    }

    public LiveData<Movie> getMovie() {
        return movieRepository.getMovieByID(movieID);
    }

    @Override
    public LiveData<List<Actor>> getActors() {
        return actors;
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
    protected void onCleared() {
        super.onCleared();
    }
}
