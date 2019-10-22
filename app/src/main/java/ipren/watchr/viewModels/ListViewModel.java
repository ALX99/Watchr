package ipren.watchr.viewModels;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.List;

import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.MovieRepository;

import static java.lang.Integer.parseInt;

/**
 * The view model for the movies lists, handling the data conversion
 */
public class ListViewModel {

    // Singleton instance
    private static ListViewModel instance;

    // Repositories
    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    // Live data from user repo
    private LiveData<User> user;
    private LiveData<String[]> movieIds;
    private LiveData<String[]> favoritesIds;
    private LiveData<String[]> watchLaterIds;
    private LiveData<String[]> watchedIds;

    // Live data from movie repo
    private LiveData<List<Movie>> movies;

    // Local live data
    private MutableLiveData<Boolean> loadingStatus;
    private MutableLiveData<Boolean> emptyListStatus;
    private MutableLiveData<Boolean> loggedInStatus;

    // Local variables
    private String listType;

    private ListViewModel(Fragment fragment) {
        userRepository = IUserDataRepository.getInstance();
        movieRepository = IMovieRepository.getInstance();
        user = userRepository.getUserLiveData();

        loadingStatus = new MutableLiveData<>();
        emptyListStatus = new MutableLiveData<>();
        loggedInStatus = new MutableLiveData<>();
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    private void setPersonalList(String listType) {
        if (user.getValue() == null) {
            loggedInStatus.setValue(false);
            emptyListStatus.setValue(false);
        } else {
            movieIds = userRepository.getMovieList(listType, user.getValue().getUID());
            if (movieIds.getValue() == null || movieIds.getValue().length == 0) {
                // Empty list
                emptyListStatus.setValue(true);
            } else {
                // Set list
                int[] movieIdsInt = convertStringArrayToIntArray(movieIds.getValue());
                movies = movieRepository.getMoviesByID(movieIdsInt);
            }
        }
    }

    /**
     * Where the magic happens, sets up the correct movie list based on type and user status
     */
    public void refresh() {
        if (user.getValue() != null) {
            favoritesIds = userRepository.getMovieList(IUserDataRepository.FAVORITES_LIST, user.getValue().getUID());
            watchLaterIds = userRepository.getMovieList(IUserDataRepository.WATCH_LATER_LIST, user.getValue().getUID());
            watchedIds = userRepository.getMovieList(IUserDataRepository.WATCHED_LIST, user.getValue().getUID());
        }

        switch (listType) {
            case IMovieRepository.BROWSE_LIST:
                movies = movieRepository.getMovieList(MovieRepository.TRENDING_LIST, 1, true);
                break;
            case IMovieRepository.RECOMMENDED_LIST:
                // TODO: @johan Make fetching from discover list work
                movies = movieRepository.getDiscoverList(new int[]{27}, 1, true);
                break;
            case IUserDataRepository.FAVORITES_LIST:
            case IUserDataRepository.WATCH_LATER_LIST:
            case IUserDataRepository.WATCHED_LIST:
                setPersonalList(listType);
                break;
        }
    }

    /**
     * @return Singleton instance
     */
    public static ListViewModel getInstance(Fragment fragment) {
        if (instance == null) {
            instance = new ListViewModel(fragment);
        }
        return instance;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<Boolean> getLoadingStatus() {
        return loadingStatus;
    }

    public MutableLiveData<Boolean> getEmptyListStatus() {
        return emptyListStatus;
    }

    public MutableLiveData<Boolean> getLoggedInStatus() {
        return loggedInStatus;
    }

    public LiveData<List<Movie>> getMoviesFromQuery(String query) {
        // TODO: @johan Get query from correct method when it's created
        return movieRepository.getMovieList(query, 1, true);
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
    private int[] convertStringArrayToIntArray(String[] strArr) {
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = parseInt(strArr[i]);
        }
        return intArr;
    }
}
