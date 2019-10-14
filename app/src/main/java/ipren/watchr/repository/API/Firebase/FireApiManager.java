package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.UserDataAPI;
import ipren.watchr.repository.IMainRepository;

public class FireApiManager implements UserDataAPI {

    private static UserDataAPI fireApiManager;

    private FirebaseAuthAPI firebaseAuthAPI;

    private FirebaseDatabaseHelper firestoreDatabase;

    private LiveData<User> currentLoggedUser;

    public static UserDataAPI getInstance() {
        if (fireApiManager == null)
            fireApiManager = new FireApiManager();
        return fireApiManager;
    }

    private FireApiManager() {
        firebaseAuthAPI = new FirebaseAuthAPI();
        firestoreDatabase = new FirebaseDatabaseHelper();
        currentLoggedUser = firebaseAuthAPI.getUser();

        currentLoggedUser.observeForever(user -> {
            if (user == null)
                return;
            else
                firestoreDatabase.syncUserWithDatabase(user);
        });
    }

    //Fire Auth

    @Override
    public LiveData<User> getUserLiveData() {
        return currentLoggedUser;
    }

    @Override
    public void registerUser(String email, String password, OnCompleteListener callback) {
        firebaseAuthAPI.registerUser(email, password, callback);
    }

    @Override
    public void signOutUser() {
        firebaseAuthAPI.signOut();
    }

    @Override
    public void loginUser(String email, String password, OnCompleteListener callback) {
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

    //Firestore

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return firestoreDatabase.getPublicProfile(user_id);
    }

    @Override
    public LiveData<FireComment[]> getComments(String id, int searchMethod) {
        if (searchMethod == IMainRepository.SEARCH_METHOD_MOVIE_ID)
            return firestoreDatabase.getCommentByMovieID(id);
        else
            return firestoreDatabase.getCommentsByUserID(id);
    }

    @Override
    public LiveData<FireRating[]> getRatings(String id, int searchMethod) {
        if (searchMethod == IMainRepository.SEARCH_METHOD_MOVIE_ID)
            return firestoreDatabase.getRatingByMovieID(id);
        else
            return firestoreDatabase.getRatingByUserID(id);
    }

    @Override
    public void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        firestoreDatabase.saveMovieToList(list, movie_id, user_id, callback);
    }

    @Override
    public void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        firestoreDatabase.deleteMovieFromList(list, movie_id, user_id, callback);
    }

    @Override
    public void rateMovie(int score, String movie_id, String user_id, OnCompleteListener callback) {
        if (score > 10)
            score = 10;
        else if (score < 0)
            score = 0;

       firestoreDatabase.addRating(score, movie_id, user_id, callback);

    }

    @Override
    public void removeRating(String rating_id, OnCompleteListener callback) {
        firestoreDatabase.removeRating(rating_id, callback);
    }

    @Override
    public void commentMovie(String text, String movie_id, String user_id, OnCompleteListener callback) {
        firestoreDatabase.addComment(text, movie_id, user_id, callback);
    }

    @Override
    public void removeComment(String comment_id, OnCompleteListener callback) {
        firestoreDatabase.removeComment(comment_id, callback);
    }
}
