package ipren.watchr.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.MovieRepository;

/**
 *
 */
public class ListViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
=======
    public MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    public MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
>>>>>>> Stashed changes

    private LiveData<User> user;
    private LiveData<String[]> watchLaterIds;
    private LiveData<String[]> watchedIds;
    private LiveData<String[]> favoritesIds;

    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    // Collects disposable single observers and disposes them
    private CompositeDisposable disposable = new CompositeDisposable();

    // We use AndroidViewModel to get access to a context for storing data
    public ListViewModel(@NonNull Application application) {
        super(application);
        // TODO: implementera mot repository
        movieRepository = new MovieRepository(application.getApplicationContext());
        userRepository = IUserDataRepository.getInstance();
        user = userRepository.getUserLiveData();
    }

    public void getList(String list, int page) {
        movies = movieRepository.getMovieList(list, page, false);
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

    public void refresh(String list, int page) {
        movieRepository.getMovieList(list, page, true);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    private void fetchFromDatabase() {

    }
}
