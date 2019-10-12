package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.UserDataAPI;

public class FireAPI implements UserDataAPI {
    FireAPI fireAPI;

    UserDataAPI userDataAPI;

    public UserDataAPI getFireBaseAPI(){
            if(userDataAPI == null)
                userDataAPI = new FireAPI();
            return fireAPI;
    }


    @Override
    public LiveData<User> getUserLiveData() {
        return null;
    }

    @Override
    public void registerUser(String email, String password, OnCompleteListener callback) {

    }

    @Override
    public void signOutUser() {

    }

    @Override
    public void loginUser(String email, String password, OnCompleteListener callback) {

    }

    @Override
    public void refreshUsr() {

    }

    @Override
    public void reSendVerificationEmail() {

    }

    @Override
    public void updateProfile(String userName, Uri pictureUri) {

    }

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return null;
    }

    @Override
    public LiveData<FireComment> getComments(String movie_id, int searchMethod) {
        return null;
    }

    @Override
    public LiveData<FireRating> getRatings(String movie_id, int searchMethod) {
        return null;
    }

    @Override
    public void addMovieToList(String list, String movie_id, String user_id) {

    }

    @Override
    public void removeMovieFromList(String list, String movie_id, String user_id) {

    }

    @Override
    public void rateMovie(int score, String movie_id, String user_id) {

    }

    @Override
    public void removeRating(String rating_id) {

    }

    @Override
    public void commentMovie(String text, String movie_id, String user_id) {

    }

    @Override
    public void removeComment(String comment_id) {

    }
}
