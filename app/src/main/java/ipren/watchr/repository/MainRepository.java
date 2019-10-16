package ipren.watchr.repository;


import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.UserDataAPI;

public class MainRepository implements IMainRepository {

    private static final IMainRepository MAIN_REPOSITORY = new MainRepository();
    UserDataAPI userDataApi;

    private MainRepository() {
        userDataApi = UserDataAPI.getInstance();

    }

    public static IMainRepository getMainRepository() {
        return MAIN_REPOSITORY;
    }

    public LiveData<User> getUserLiveData() {
        return userDataApi.getUserLiveData();
    }

    public void registerUser(String email, String password, OnCompleteListener callback) {
        userDataApi.registerUser(email, password, callback);
    }

    public void signOutUser() {
        userDataApi.signOutUser();
    }

    public void loginUser(String email, String password, OnCompleteListener callback) {
        userDataApi.loginUser(email, password, callback);
    }

    @Override
    public void refreshUsr() {
        userDataApi.refreshUsr();
    }

    @Override
    public void reSendVerificationEmail() {
        userDataApi.reSendVerificationEmail();
    }

    @Override
    public void updateProfile(String userName, Uri pictureUri) {
        userDataApi.updateProfile(userName, pictureUri);
    }

    @Override
    public LiveData<FireComment[]> getComments(String movie_id, int searchMethod) {
        return userDataApi.getComments(movie_id, searchMethod);
    }

    @Override
    public LiveData<FireRating[]> getRatings(String movie_id, int searchMethod) {
        return userDataApi.getRatings(movie_id, searchMethod);
    }

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return userDataApi.getPublicProfile(user_id);
    }

    @Override
    public void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        userDataApi.addMovieToList(list, movie_id, user_id, callback);
    }

    @Override
    public void getMovieList(String list, String user_id) {
        userDataApi.getMovieListByUserId(list, user_id);
    }

    @Override
    public void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        userDataApi.removeMovieFromList(list, movie_id, user_id, callback);
    }

    @Override
    public void rateMovie(int score, String movie_id, String user_id, OnCompleteListener callback) {
        userDataApi.rateMovie(score, movie_id, user_id, callback);
    }

    @Override
    public void removeRating(String rating_id, OnCompleteListener callback) {
        userDataApi.removeRating(rating_id, callback);
    }

    @Override
    public void commentMovie(String text, String movie_id, String user_id, OnCompleteListener callback) {
        userDataApi.commentMovie(text, movie_id, user_id, callback);
    }

    @Override
    public void removeComment(String comment_id, OnCompleteListener callback) {
        userDataApi.removeComment(comment_id, callback);
    }
}