package ipren.watchr.MockClasses;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
//TODO remake this test

public class MockUserDataRepository implements IUserDataRepository {

    private Task authResponse;
    /**
     *
     * @param user Initial user
     */
    public MockUserDataRepository(User user){
       userLiveData = new MutableLiveData<>(user);
       MockTaskResponse mockTaskResponse = new MockTaskResponse();
       mockTaskResponse.setSuccessful(false);
       authResponse = mockTaskResponse;
    }

    public MockUserDataRepository(){}

    MutableLiveData<User> userLiveData = new MutableLiveData<>(null);

    public void setUser(User user){
        this.userLiveData.postValue(user);
    }

    public void setAuthCallBack(Task callbackRes){
        authResponse = callbackRes;
    }
    @Override
    public LiveData<User> getUserLiveData() {
        return this.userLiveData;
    }

    @Override
    public void resetPassword(String email, OnCompleteListener callback) {

    }

    @Override
    public void registerUser(String email, String password, OnCompleteListener callback) {
        this.userLiveData.postValue(new User("unkown",email, null, null, false));
        callback.onComplete(authResponse);
    }

    @Override
    public void signOutUser() {
        this.userLiveData.postValue(null);
    }

    @Override
    public void loginUser(String email, String password, OnCompleteListener callback) {
        this.userLiveData.postValue(new User("unkown",email, null, null, false));
        callback.onComplete(authResponse);
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
    public LiveData<Comment[]> getComments(String movie_id, int searchMethod) {
        return null;
    }

    @Override
    public LiveData<Rating[]> getRatings(String movie_id, int searchMethod) {
        return null;
    }

    @Override
    public LiveData<String[]> getMovieList(String list, String user_id) {
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