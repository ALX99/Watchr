package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import static ipren.watchr.repository.IUserDataRepository.*;

public class AccountViewModel extends ViewModel {

    LiveData<User> user;

    private IUserDataRepository userDataRepository;
    @VisibleForTesting
    public AccountViewModel(IUserDataRepository iMainRepository) {
        userDataRepository = iMainRepository;
        this.user = userDataRepository.getUserLiveData();
    }

    public AccountViewModel() {
        userDataRepository = IUserDataRepository.getInstance();
        this.user = userDataRepository.getUserLiveData();
    }

    public void signOut() {
        userDataRepository.signOutUser();
    }

    public LiveData<User> getUser() {
        return this.user;
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


}
