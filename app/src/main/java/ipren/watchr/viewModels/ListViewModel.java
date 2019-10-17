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
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;

/**
 * The view model for the movies lists, handling the data conversion
 */
public class ListViewModel extends AndroidViewModel {
    // ----------------NYA----------------
    private LiveData<User> user;
    private LiveData<String[]> favoritesIds;
    private LiveData<String[]> watchLaterIds;
    private LiveData<String[]> watchedIds;
    private LiveData<List<Movie>> movieList;
    private MutableLiveData<Boolean> movieLoadError;
    private MutableLiveData<Boolean> loading;
    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;
    private boolean isLoggedIn;
    private String listType;

    //----------------GAMLA---------------
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MovieApi movieService = new MovieApi();
    private CompositeDisposable disposable = new CompositeDisposable();

    // We use AndroidViewModel to get access to a context for storing data
    public ListViewModel(@NonNull Application application) {
        super(application);
        userRepository = IUserDataRepository.getInstance();
        movieLoadError = new MutableLiveData<>();
        loading = new MutableLiveData<>();
//        movieRepository = IMovieRepository.getInstace();
    }


    // ----------------NYA--------------------------------------------------------------
    public void initData() {
        user = userRepository.getUserLiveData();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public boolean getLoggedInStatus() {
        return isLoggedIn;
    }

    public void setLoggedInStatus(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public MutableLiveData<Boolean> getMovieLoadError() {
        return movieLoadError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setListType(String listType) {
        this.listType = listType;
        if (isLoggedIn) {
            switch (listType) {
                case "favorites":
                    favoritesIds = userRepository.getMovieList(listType, user.getValue().getUID());
                case "watchLater":
                    watchLaterIds = userRepository.getMovieList(listType, user.getValue().getUID());
                case "watched":
                    watchedIds = userRepository.getMovieList(listType, user.getValue().getUID());
            }
        } else {

        }
    }

    //----------------GAMLA-------------------------------------------------------------
    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
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
}
