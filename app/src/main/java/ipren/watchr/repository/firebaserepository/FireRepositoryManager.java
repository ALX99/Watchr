package ipren.watchr.repository.firebaserepository;

import android.net.Uri;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;


import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.IUserDataRepository;
//This class manages request to both FireBaseAuthHelper and FireBaseDataBaseManager as well as syncing data with the two API's
//This class functions as controller/manager for the repository
public class FireRepositoryManager implements IUserDataRepository {
    //Singleton
    private static FireRepositoryManager fireApiManager;

    private FireBaseAuthHelper firebaseAuthHelper;

    private FireBaseDatabaseHelper firestoreDatabase;

    private LiveData<User> currentLoggedUser;

    @VisibleForTesting
    FireRepositoryManager(FireBaseAuthHelper firebaseAuthHelper, FireBaseDatabaseHelper firestoreDatabase){
        this.firebaseAuthHelper = firebaseAuthHelper;
        this.firestoreDatabase = firestoreDatabase;
    }
    //Initiates the repository. Syncs user data changes in FireBaseAuthHelper to FireBaseDatabaseHelper
    private FireRepositoryManager() {
        firebaseAuthHelper = new FireBaseAuthHelper();
        firestoreDatabase = new FireBaseDatabaseHelper();
        currentLoggedUser = firebaseAuthHelper.getUser();

        currentLoggedUser.observeForever(user -> {
            if (user == null)
                return;
            else
                firestoreDatabase.syncUserWithDatabase(user); //Syncing user data with FireBaseDatabaseHelper creating a public profile for other users to see.
        });
    }
    //Returns an instance of the repositry
    public static IUserDataRepository getInstance() {
        if (fireApiManager == null)
            fireApiManager = new FireRepositoryManager();
        return fireApiManager;
    }

    //FireBaseAuthHelper methods

    @Override
    public LiveData<User> getUserLiveData() {
        return currentLoggedUser;
    }

    @Override
    public void resetPassword(String email ,OnCompleteListener callback) {
        firebaseAuthHelper.resetPassword(email , callback);
    }

    @Override
    public void registerUser(String email, String password, OnCompleteListener callback) {
        firebaseAuthHelper.registerUser(email, password, callback);
    }

    @Override
    public void signOutUser() {
        firebaseAuthHelper.signOut();
    }

    @Override
    public void loginUser(String email, String password, OnCompleteListener callback) {
        firebaseAuthHelper.loginUser(email, password, callback);
    }

    @Override
    public void refreshUsr() {
        firebaseAuthHelper.refreshUsr();
    }

    @Override
    public void reSendVerificationEmail(OnCompleteListener callback) {
        firebaseAuthHelper.resendVerificationEmail(callback);
    }

    @Override
    public void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback) {
        firebaseAuthHelper.updateProfile(userName, pictureUri, callback);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
        firebaseAuthHelper.changePassword(oldPassword,newPassword, callback);
    }

    //Firestore

    @Override
    public LiveData<PublicProfile> getPublicProfile(String user_id) {
        return firestoreDatabase.getPublicProfile(user_id);
    }

    //Checks what search method they are using and calls corresponding method in the database
    @Override
    public LiveData<Comment[]> getComments(String id, int searchMethod) {
        if (searchMethod == IUserDataRepository.SEARCH_METHOD_MOVIE_ID)
            return firestoreDatabase.getCommentByMovieID(id);
        else
            return firestoreDatabase.getCommentsByUserID(id);
    }
    //Checks what search method they are using and calls corresponding method in the database
    @Override
    public LiveData<Rating[]> getRatings(String id, int searchMethod) {
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
    public LiveData<String[]> getMovieList(String list, String id) {
        return firestoreDatabase.getMovieListByUserID(list, id);
    }

    @Override
    public void removeMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        firestoreDatabase.deleteMovieFromList(list, movie_id, user_id, callback);
    }
    //Processes the input score before passing it to the database
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