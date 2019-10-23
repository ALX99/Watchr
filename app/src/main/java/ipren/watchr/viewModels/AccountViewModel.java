package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;

import static ipren.watchr.repository.IUserDataRepository.FAVORITES_LIST;
import static ipren.watchr.repository.IUserDataRepository.SEARCH_METHOD_USER_ID;
import static ipren.watchr.repository.IUserDataRepository.WATCHED_LIST;
import static ipren.watchr.repository.IUserDataRepository.WATCH_LATER_LIST;

public class AccountViewModel extends ViewModel {

    public final LiveData<User> user;

    private IUserDataRepository userDataRepository;
    private IMovieRepository movieRepository;
    @VisibleForTesting
    public AccountViewModel(IUserDataRepository iMainRepository, IMovieRepository movieRepository) {
        userDataRepository = iMainRepository;
        this.movieRepository = movieRepository;
        this.user = userDataRepository.getUserLiveData();
    }

    public AccountViewModel() {
        userDataRepository = IUserDataRepository.getInstance();
        movieRepository = IMovieRepository.getInstance();
        this.user = userDataRepository.getUserLiveData();
    }

    public void signOut() {
        userDataRepository.signOutUser();
    }

    public LiveData<Rating[]> getRatingByUserId(String user_id){
          return userDataRepository.getRatings(user_id,SEARCH_METHOD_USER_ID);
    }

    public LiveData<Comment[]> getCommentsByUserId(String user_id){
        return userDataRepository.getComments(user_id,SEARCH_METHOD_USER_ID);
    }

    public LiveData<String[]> getFavoritesList(String user_id){
        return userDataRepository.getMovieList(FAVORITES_LIST, user_id);
    }
    public LiveData<String[]> getWatchedList(String user_id){
        return userDataRepository.getMovieList(WATCHED_LIST, user_id);
    }

    public LiveData<String[]> getWatchLaterList(String user_id){
        return userDataRepository.getMovieList(WATCH_LATER_LIST, user_id);
    }

    public LiveData<List<Movie>> getMovies(int[] ids) {
        return movieRepository.getMoviesByID(ids);
    }


}
