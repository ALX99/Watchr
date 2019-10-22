package ipren.watchr.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.Firebase.FireRepositoryManager;

public interface IUserDataRepository {
    int SEARCH_METHOD_MOVIE_ID = 0;
    int SEARCH_METHOD_USER_ID = 1;
    String FAVORITES_LIST = "Favorites";
    String WATCH_LATER_LIST = "Watch_Later";
    String WATCHED_LIST = "Watched";

    static IUserDataRepository getInstance() {
        return FireRepositoryManager.getInstance();
    }

    //Firebase auth functions

    LiveData<User> getUserLiveData();

    void resetPassword(String email, OnCompleteListener callback );

    void registerUser(String email, String password, OnCompleteListener callback);

    void signOutUser();

    void loginUser(String email, String password, OnCompleteListener callback);

    void refreshUsr();

    void reSendVerificationEmail(OnCompleteListener callback);

    void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback);

    void changePassword(String oldPassword, String newPassword , OnCompleteListener callback);

    //Firebase database functions
    LiveData<PublicProfile> getPublicProfile(String user_id);

    LiveData<Comment[]> getComments(String movie_id, int searchMethod);

    LiveData<Rating[]> getRatings(String movie_id, int searchMethod);

    LiveData<String[]> getMovieList(String list, String user_id);

    void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback);

    void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback);

    void rateMovie(int score, String movie_id, String user_id, OnCompleteListener callback);

    void removeRating(String rating_id, OnCompleteListener callback);

    void commentMovie(String text, String movie_id, String user_id, OnCompleteListener callback);

    void removeComment(String comment_id, OnCompleteListener callback);


}
