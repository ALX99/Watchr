package ipren.watchr.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.firebase.FireRepositoryManager;

//Interface used for managing user related data such as
public interface IUserDataRepository {
    int SEARCH_METHOD_MOVIE_ID = 0; //Notifies that the String provided is a MOVIE_ID
    int SEARCH_METHOD_USER_ID = 1; //Notifies that the String provided is a USER_ID
    String FAVORITES_LIST = "Favorites"; // Static movie-list
    String WATCH_LATER_LIST = "Watch_Later"; // Static movie-list
    String WATCHED_LIST = "Watched"; // Static movie-list

    static IUserDataRepository getInstance() {
        return FireRepositoryManager.getInstance();
    }

    //Firebase auth functions

    LiveData<User> getUserLiveData();

    void resetPassword(String email, OnCompleteListener callback);

    void registerUser(String email, String password, OnCompleteListener callback);

    void signOutUser();

    void loginUser(String email, String password, OnCompleteListener callback);

    void refreshUsr();

    void reSendVerificationEmail(OnCompleteListener callback);

    void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback);

    void changePassword(String oldPassword, String newPassword, OnCompleteListener callback);

    //Firebase database functions
    LiveData<PublicProfile> getPublicProfile(String user_id);

    LiveData<Comment[]> getComments(String id, int searchMethod);

    LiveData<Rating[]> getRatings(String id, int searchMethod);

    LiveData<String[]> getMovieList(String list, String id);

    void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback);

    void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback);

    void rateMovie(int score, String movie_id, String user_id, OnCompleteListener callback);

    void removeRating(String rating_id, OnCompleteListener callback);

    void commentMovie(String text, String movie_id, String user_id, OnCompleteListener callback);

    void removeComment(String comment_id, OnCompleteListener callback);


}
