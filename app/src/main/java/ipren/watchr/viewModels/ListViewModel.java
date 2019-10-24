package ipren.watchr.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.MovieRepository;

import static java.lang.Integer.parseInt;

/**
 * The view model for the movies lists, handling the data conversion
 */
public class ListViewModel extends ViewModel {

    // Repositories
    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    // Live data from user repo
    private LiveData<User> user;
    private LiveData<String[]> favoritesIds;
    private LiveData<String[]> watchLaterIds;
    private LiveData<String[]> watchedIds;

    // Live data from movie repo
    private LiveData<List<Movie>> movies;

    public ListViewModel() {
        userRepository = IUserDataRepository.getInstance();
        // TODO: @johan Fixa detta!!
        movieRepository = IMovieRepository.getInstance();
        user = userRepository.getUserLiveData();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Movie>> getMoviesFromQuery(String query) {
        return movieRepository.Search(query, 1, false);
    }

    public boolean checkMovieInList(int movieId, String listType) {
        switch (listType) {
            case IUserDataRepository.FAVORITES_LIST: return Arrays.asList(favoritesIds.getValue()).contains(String.valueOf(movieId));
            case IUserDataRepository.WATCH_LATER_LIST: return Arrays.asList(watchLaterIds.getValue()).contains(String.valueOf(movieId));
            case IUserDataRepository.WATCHED_LIST: return Arrays.asList(watchedIds.getValue()).contains(String.valueOf(movieId));
            default: return false;
        }
    }

    /**
     * Returns 1 if user is logged in and movie not in list
     * Returns 0 if user is not logged in
     * Returns -1 if user is logged in and movie is in list
     */
    public int updateMovieInList(int movieId, String listType) {
        if (user.getValue() != null) {
            LiveData<String[]> ids = null;
            switch (listType) {
                case IUserDataRepository.FAVORITES_LIST: ids = favoritesIds;
                case IUserDataRepository.WATCH_LATER_LIST: ids = watchLaterIds;
                case IUserDataRepository.WATCHED_LIST: ids = watchedIds;
            }
            if (Arrays.asList(ids.getValue()).contains(String.valueOf(movieId))) {
                userRepository.removeMovieFromList(listType, "" + movieId, user.getValue().getUID(), v -> Log.d("TEST", "Removed movie with id " + movieId + " to " + listType));
                return -1;
            } else {
                userRepository.addMovieToList(listType, "" + movieId, user.getValue().getUID(), v -> Log.d("TEST", "Added movie with id " + movieId + " to " + listType));
            }
            return 1;
        }
        return 0;
    }

    /**
     * Converts an array of integer strings to an array of ints
     */
    // TODO: @johan Maybe put this in util?
    private int[] convertStringArrayToIntArray(String[] strArr) {
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = parseInt(strArr[i]);
        }
        return intArr;
    }

    public void initBrowse() {
        movies = movieRepository.getMovieList(IMovieRepository.TRENDING_LIST, 1, false);
    }

    public void initRecommended() {
        movies = movieRepository.getDiscoverList(new int[]{27}, 1, false);
    }

    public void initMovieIdLists() {
        String userId = user.getValue().getUID();
        watchedIds = userRepository.getMovieList(IUserDataRepository.WATCHED_LIST, userId);
        watchLaterIds = userRepository.getMovieList(IUserDataRepository.WATCH_LATER_LIST, userId);
        favoritesIds = userRepository.getMovieList(IUserDataRepository.FAVORITES_LIST, userId);
    }

    public LiveData<String[]> getWatchedIds() {
        return watchedIds;
    }

    public LiveData<String[]> getWatchLaterIds() {
        return watchLaterIds;
    }

    public LiveData<String[]> getFavoritesIds() {
        return favoritesIds;
    }

    public void initUserMovieList(String[] movieIds) {
        int[] movieIdsInt = convertStringArrayToIntArray(movieIds);
        movies = movieRepository.getMoviesByID(movieIdsInt);
    }

    public LiveData<List<Genre>> getGenres(int movieId) {
        return movieRepository.getGenresFromMovie(movieId);
    }
}
