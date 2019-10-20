package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;

public class FireRepositoryManager implements IUserDataRepository {

    private static IUserDataRepository fireApiManager;

    private FirebaseAuthAPI firebaseAuthAPI;

    private FirebaseDatabaseHelper firestoreDatabase;

    private LiveData<User> currentLoggedUser;

    private FireRepositoryManager() {
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

    public static IUserDataRepository getInstance() {
        if (fireApiManager == null)
            fireApiManager = new FireRepositoryManager();
        return fireApiManager;
    }

    //Fire Auth

    @Override
    public LiveData<User> getUserLiveData() {
        return currentLoggedUser;
    }

    @Override
    public void resetPassword(String email ,OnCompleteListener callback) {
        firebaseAuthAPI.resetPassword(email , callback);
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
    public void reSendVerificationEmail(OnCompleteListener callback) {
        firebaseAuthAPI.resendVerificationEmail(callback);
    }

    @Override
    public void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback) {
        firebaseAuthAPI.updateProfile(userName, pictureUri, callback);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
            firebaseAuthAPI.changePassword(oldPassword,newPassword, callback);
    }

    //Firestore

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return firestoreDatabase.getPublicProfile(user_id);
    }

    @Override
    public LiveData<FireComment[]> getComments(String id, int searchMethod) {
        if (searchMethod == IUserDataRepository.SEARCH_METHOD_MOVIE_ID)
            return firestoreDatabase.getCommentByMovieID(id);
        else
            return firestoreDatabase.getCommentsByUserID(id);
    }

    @Override
    public LiveData<FireRating[]> getRatings(String id, int searchMethod) {
        if (searchMethod == IUserDataRepository.SEARCH_METHOD_MOVIE_ID)
            return firestoreDatabase.getRatingByMovieID(id);
        else
            return firestoreDatabase.getRatingByUserID(id);
    }

    @Override
    public void addMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        firestoreDatabase.saveMovieToList(list, movie_id, user_id, callback);
    }

    @Override
    public LiveData<String[]> getMovieList(String list, String user_id) {
        return firestoreDatabase.getMovieListByUserID(list, user_id);
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
