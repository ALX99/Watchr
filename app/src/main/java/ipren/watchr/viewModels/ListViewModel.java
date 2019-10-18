package ipren.watchr.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.IllegalFormatConversionException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ipren.watchr.BuildConfig;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieList;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.MovieApi;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.MovieRepository;

import static java.lang.Integer.parseInt;

/**
 * The view model for the movies lists, handling the data conversion
 */
public class ListViewModel extends AndroidViewModel {

    private LiveData<User> user;
    private LiveData<String[]> movieIds;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> movieLoadError;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Boolean> emptyListStatus;
    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    // We use AndroidViewModel to get access to a context for Room
    public ListViewModel(@NonNull Application application) {
        super(application);

        userRepository = IUserDataRepository.getInstance();
        movieRepository = new MovieRepository(application.getApplicationContext());
        user = userRepository.getUserLiveData();
        movieLoadError = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        emptyListStatus = new MutableLiveData<>();
        movies = new MutableLiveData<>();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<Boolean> getMovieLoadError() {
        return movieLoadError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getEmptyListStatus() { return emptyListStatus; }

    /**
     * Returns 1 if user is logged in and movie not in list
     * Returns 0 if user is not logged in
     * Returns -1 if user is logged in and movie is in list
     */
    public int updateMovieInList(int movieId, String listType) {
        if (user.getValue() != null) {
            LiveData<String[]> ids = userRepository.getMovieList(listType, user.getValue().getUID());
            if (Arrays.asList(ids.getValue()).contains(movieId)) {
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
     * Fetches the correct list and updates the corresponding live data
     */
    public void refresh(String listType) {
        loading.setValue(true);

        if (listType.equals("Browse")) {
            // Fetch browse list
            movies = movieRepository.getMovieList(MovieRepository.TRENDING_LIST, 1, true);
        } else if (listType.equals("Recommended")) {
            // Fetch recommended
            movies = movieRepository.getMovieList(MovieRepository.TRENDING_LIST, 1, true);
        } else {
            // Get list ids
            movieIds = userRepository.getMovieList(listType, user.getValue().getUID());
            if (movieIds.getValue() == null || movieIds.getValue().length == 0) {
                emptyListStatus.setValue(true);
            } else {
                int[] movieIdsInt = convertStringArrayToIntArray(movieIds.getValue());
                // Get movies from ids
                movies = movieRepository.getMoviesByID(movieIdsInt);
            }
        }

        loading.setValue(false);
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
