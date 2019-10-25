package ipren.watchr.repository.firebase;

import android.net.Uri;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ipren.watchr.dataholders.User;

//This class manages (Logged)user authentication/management
class FireBaseAuthHelper {
    private final MutableLiveData userLiveData = new MutableLiveData(null); //LiveData representing the signed in user, null = no user signed in.
    private FirebaseAuth mAuth;  //An instance used for all firebase user interaction

    private final static String FIRESTORE_PIC_FOLDER = "pics/"; //Path to storage where uploaded picture are


    @VisibleForTesting
    FireBaseAuthHelper(FirebaseAuth mAuth) {
        this.mAuth = mAuth;

    }

    //A constructor sett variables and setting a listener for when a user is logged in our out (authStateListener).
    FireBaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(e -> refreshUsr());
        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(2000);
    }

    // Gets the newest user data if a user is logged in and posts it to LiveData
    // The "reload()" method does not trigger the AuthstateListener so LiveData must be updated manually
    void refreshUsr() {
        if (mAuth.getCurrentUser() != null)
            mAuth.getCurrentUser().reload().addOnCompleteListener(e ->
                    this.userLiveData.postValue(buildUserObject(mAuth.getCurrentUser())));
        else
            this.userLiveData.postValue(null);

    }

    //This method sends a verification email to the current signed in user, if the user is already verified  or not logged in returns null
    //Results are sent to callback if present
    void resendVerificationEmail(OnCompleteListener callback) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null || firebaseUser.isEmailVerified()) {
            triggerCallback(callback, null);
            return;
        }

        attachCallback(firebaseUser.sendEmailVerification(), callback);
    }

    //This method returns the LiveData object representing the current logged in user
    LiveData<User> getUser() {
        return this.userLiveData;
    }

    //This method attempts to sign in a user with provided credentials. Results are sent to callback if present. Will trigger AuthStateListener if successful
    void loginUser(String email, String password, OnCompleteListener callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback);
    }

    //This method attempts to register an account with  provided credentials. If successful attempts to send an email verification and triggering AuthStateListener, signing in the user.  Results are sent to callback if present
    void registerUser(String email, String password, OnCompleteListener callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                task.getResult().getUser().sendEmailVerification();

            triggerCallback(callback, task);
        });
    }

    //Signs the user out, no callback required. Triggers AuthStateListener
    void signOut() {
        mAuth.signOut();
    }

    //Attempts to send a password reset link to the provided email, results are passed to callback if present.
    void resetPassword(String email, OnCompleteListener callback) {
        attachCallback(mAuth.sendPasswordResetEmail(email), callback);
    }

    // This method attempts to change the current users password. By first re-authenticating the user, and if it succeeds attempting to change the password.  Results are sent to callback if present
    void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            triggerCallback(callback, null);
            return;
        }

        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword)).addOnCompleteListener(e -> {
            if (e.isSuccessful())
                attachCallback(user.updatePassword(newPassword), callback);
            else
                triggerCallback(callback, e);

        });
    }



    //This method starts the update profile process. Preparing the data.
    // If a picture URI is present, it will attempt to upload it before updating the profile, if it fails it will send results to the callback if it is present. If it succeeds it will attempt to upload the changes
    //If no Uri is present it will try to upload the changes right away
    void updateProfile(String userName, Uri uri, OnCompleteListener callback) {
        if (uri != null) {
            uploadImage(uri, e -> {
                if (e.isSuccessful())
                    uploadProfileChanges(userName, (Uri) e.getResult(), callback);
                else
                    triggerCallback(callback,e);

            });
        } else {
            uploadProfileChanges(userName, null, callback);
        }
    }


    //Uploads an image to FireBase storage, the callback either returns a failure or it attempts to
    // get the URI to the uploaded image and then passing the result from that task to the callback
    private void uploadImage(Uri uri, OnCompleteListener callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().
                getReference().child(FIRESTORE_PIC_FOLDER + mAuth.getCurrentUser().getUid()); // Address

        storageRef.putFile(uri).addOnCompleteListener(e -> {
            if (e.isSuccessful())
                storageRef.getDownloadUrl().addOnCompleteListener(callback);  //Attempt to get uploaded picture URI, send results to callback
            else
                callback.onComplete(e); // Failed to upload picture trigger callback immediately
        });
    }

    // This method takes in a username and an URI and attempts to upload them to the current users account
    //results are passed to the callback.
    private void uploadProfileChanges(String userName, Uri uri, OnCompleteListener callback) {
        if (mAuth.getCurrentUser() == null) {
            triggerCallback(callback, null);
            return;
        }
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        if (uri != null)
            builder.setPhotoUri(uri);
        if (userName != null)
            builder.setDisplayName(userName);

        mAuth.getCurrentUser().updateProfile(builder.build()).addOnCompleteListener(e -> {
            if (e.isSuccessful())
                refreshUsr();

            triggerCallback(callback, e);

        });

    }


    //Helper method for constructing the Immutable User object representing the user.
    private User buildUserObject(FirebaseUser firebaseUser) {
        if (firebaseUser == null)
            return null;

        String UID = firebaseUser.getUid();
        Boolean isVerified = firebaseUser.isEmailVerified();
        String email = firebaseUser.getEmail();
        String userName = firebaseUser.getDisplayName();
        Uri profilePicture = firebaseUser.getPhotoUrl();
        return new User(userName, email, profilePicture, UID, isVerified);
    }


    //Attaches a callback to a task if the callback is not null;
    private void attachCallback(Task task, OnCompleteListener callback) {
        if (callback != null)
            task.addOnCompleteListener(callback);
    }
    //Passes value to callback if it is not null
    private void triggerCallback(OnCompleteListener callback, Task task){
        if(callback != null)
            callback.onComplete(task);
    }
}
