package ipren.watchr.viewmodels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;

import static ipren.watchr.repository.IUserDataRepository.FAVORITES_LIST;
import static ipren.watchr.repository.IUserDataRepository.SEARCH_METHOD_USER_ID;
import static ipren.watchr.repository.IUserDataRepository.WATCHED_LIST;
import static ipren.watchr.repository.IUserDataRepository.WATCH_LATER_LIST;
//This class provides data for the AccountFragment Activity.Functions as an abstraction layer between the API and activity/view
public class AccountViewModel extends ViewModel {

    public final LiveData<User> user; // The current user singed in

    private IUserDataRepository userDataRepository; //Handles user data

    private IMovieRepository movieRepository; //Handles movie information

    @VisibleForTesting
    public AccountViewModel(IUserDataRepository iMainRepository, IMovieRepository movieRepository) {
        userDataRepository = iMainRepository;
        this.movieRepository = movieRepository;
        this.user = userDataRepository.getUserLiveData();
    }
    //Default constructor used by ViewModelProviders
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
