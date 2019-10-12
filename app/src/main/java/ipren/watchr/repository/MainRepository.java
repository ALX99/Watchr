package ipren.watchr.repository;


import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.Firebase.FirebaseAuthAPI;

public class MainRepository implements IMainRepository{
    FirebaseAuthAPI firebaseAuthAPI;
    private static final IMainRepository MAIN_REPOSITORY = new MainRepository();
    public static IMainRepository getMainRepository(){
        return MAIN_REPOSITORY;
    }

    private  MainRepository(){
        firebaseAuthAPI = new FirebaseAuthAPI();
    }

    public LiveData<User> getUserLiveData(){
        return firebaseAuthAPI.getUser();
    }

    public void registerUser(String email, String password, OnCompleteListener callback){
        firebaseAuthAPI.registerUser(email,password, callback);
    }

    public void signOutUser(){
       firebaseAuthAPI.signOut();
    }

    public void loginUser(String email, String password, OnCompleteListener callback){
        firebaseAuthAPI.loginUser(email, password, callback);
    }

    @Override
    public void refreshUsr() {
        firebaseAuthAPI.refreshUsr();
    }

    @Override
    public void reSendVerificationEmail() {
        firebaseAuthAPI.resendVerificationEmail();
    }

    @Override
    public void updateProfile(String userName, Uri pictureUri) {
        firebaseAuthAPI.updateProfile(userName, pictureUri);
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
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
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