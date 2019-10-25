package ipren.watchr.MockClasses;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.IUserDataRepository;
//TODO remake this test

public class UserDataRepositoryAdapter implements IUserDataRepository {


    @Override
    public LiveData<User> getUserLiveData() {
        return null;
    }

    @Override
    public void resetPassword(String email, OnCompleteListener callback) {

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
    public void reSendVerificationEmail(OnCompleteListener callback) {

    }

    @Override
    public void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {

    }

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return null;
    }

    @Override
    public LiveData<Comment[]> getComments(String id, int searchMethod) {
        return null;
    }

    @Override
    public LiveData<Rating[]> getRatings(String id, int searchMethod) {
        return null;
    }

    @Override
    public LiveData<String[]> getMovieList(String list, String id) {
        return null;
    }

    @Override
    public void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {

    }

    @Override
    public void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {

    }

    @Override
    public void rateMovie(int score, String movie_id, String user_id, OnCompleteListener callback) {

    }

    @Override
    public void removeRating(String rating_id, OnCompleteListener callback) {

    }

    @Override
    public void commentMovie(String text, String movie_id, String user_id, OnCompleteListener callback) {

    }

    @Override
    public void removeComment(String comment_id, OnCompleteListener callback) {

    }
}