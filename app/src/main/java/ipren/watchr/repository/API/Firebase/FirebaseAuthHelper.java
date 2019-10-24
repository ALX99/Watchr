package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ipren.watchr.dataHolders.User;

class FirebaseAuthHelper {
    private final MutableLiveData userLiveData = new MutableLiveData(null);
    private FirebaseAuth mAuth;

    private final static String FIRESTORE_PIC_FOLDER = "pics/";


    @VisibleForTesting
    FirebaseAuthHelper(FirebaseAuth mAuth) {
        this.mAuth = mAuth;

    }

    FirebaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(e -> refreshUsr());
        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(2000);
    }

    // The "reload()" method does not trigger the AuthstateListener so livedata must be updated manually
    void refreshUsr() {
        if (mAuth.getCurrentUser() != null)
            mAuth.getCurrentUser().reload().addOnCompleteListener(e ->
                    this.userLiveData.postValue(buildUserObject(mAuth.getCurrentUser())));
        else
            this.userLiveData.postValue(null);

    }

    void resendVerificationEmail(OnCompleteListener callback) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null || firebaseUser.isEmailVerified()) {
            callback.onComplete(null);
            return;
        }

        attachCallback(firebaseUser.sendEmailVerification(), callback);
    }

    LiveData<User> getUser() {
        return this.userLiveData;
    }

    void loginUser(String email, String password, OnCompleteListener callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback);
    }

    void registerUser(String email, String password, OnCompleteListener callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                task.getResult().getUser().sendEmailVerification();
            if(callback != null)
                callback.onComplete(task);
        });
    }

    void signOut() {
        mAuth.signOut();
    }

    void resetPassword(String email, OnCompleteListener callback) {
        attachCallback(mAuth.sendPasswordResetEmail(email), callback);
    }


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

    void updateProfile(String userName, Uri uri, OnCompleteListener callback) {
        if (uri != null) {
            uploadImage(uri, e -> {
                if (e.isSuccessful())
                    uploadProfileChanges(userName, (Uri) e.getResult(), callback);
                else
                    callback.onComplete(e);

            });
        } else {
            uploadProfileChanges(userName, null, callback);
        }
    }

    // This method attempts to au
    void changePassword(String oldPassword, String newPassword, OnCompleteListener callback){
       FirebaseUser user = mAuth.getCurrentUser();
       if(user == null) {
           if (callback != null)
               callback.onComplete(null);
           return;
       }

       user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword)).addOnCompleteListener( e -> {
           if(e.isSuccessful())
              attachCallback(user.updatePassword(newPassword), callback);
           else if(callback != null)
               callback.onComplete(e);
       });
    }

    // This method takes in a username and an URI and attempts to upload them to the current users account
    //results are passed to the callback.
    private void uploadProfileChanges(String userName, Uri uri, OnCompleteListener callback) {
        if(mAuth.getCurrentUser() == null) {
            if (callback != null)
                callback.onComplete(null);
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

            if(callback != null)
                callback.onComplete(e);

        });

    }

    //Uploads an image to FireBase storage, the callback either returns a failure or it attempts to
    // get the URI to the uploaded image, results from that task is passed to the callback
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


    //Attaches a callback to a task if the callback is not null;
    private void attachCallback(Task task, OnCompleteListener callback) {
        if (callback != null)
            task.addOnCompleteListener(callback);

    }
}
