package ipren.watchr.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieList;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.MovieApi;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.repository.IMovieRepository;

/**
 *
 */
public class ListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private LiveData<User> user;

    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    private MovieApi movieService = new MovieApi();
    // Collects disposable single observers and disposes them
    private CompositeDisposable disposable = new CompositeDisposable();

    // We use AndroidViewModel to get access to a context for storing data
    public ListViewModel(@NonNull Application application) {
        super(application);

        // TODO: implementera mot repository
//        movieRepository = IMovieRepository.getInstace();
        userRepository = IUserDataRepository.getInstance();
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<Boolean> getMovieLoadError() {
        return movieLoadError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void refresh(String url) {
        fetchFromRemote(url);
    }

    /**
     * Fetch movies from API on a new thread, then display it on the main thread
     */
    private void fetchFromRemote(String url) {
        loading.setValue(true);
        disposable.add(
                movieService.getMovies(url)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MovieList>() {
                            @Override
                            public void onSuccess(MovieList movieList) {
                                movies.setValue(movieList.getMovies());
                                movieLoadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                movieLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    private void fetchFromDatabase() {

    }
}
