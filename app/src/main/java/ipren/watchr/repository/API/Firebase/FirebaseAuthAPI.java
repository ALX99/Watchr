package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ipren.watchr.dataHolders.User;

public class FirebaseAuthAPI {
    private final MutableLiveData userLiveData = new MutableLiveData(null);
    private FirebaseAuth mAuth;

    FirebaseAuthAPI() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(e -> refreshUsr());
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

            callback.onComplete(task);
        });
    }

    void signOut() {
        mAuth.signOut();
    }

    public void resetPassword(String email, OnCompleteListener callback) {
        Task task = mAuth.sendPasswordResetEmail(email);
        attachCallback(task, callback);
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

    private void uploadProfileChanges(String userName, Uri uri, OnCompleteListener callback) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        if (uri != null)
            builder.setPhotoUri(uri);
        if (userName != null)
            builder.setDisplayName(userName);

        mAuth.getCurrentUser().updateProfile(builder.build()).addOnCompleteListener(e -> {
            if (e.isSuccessful())
                refreshUsr();

                callback.onComplete(e);

        });

    }

    private void uploadImage(Uri uri, OnCompleteListener callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().
                getReference().child("pics/" + mAuth.getCurrentUser().getUid());

        storageRef.putFile(uri).addOnCompleteListener(e -> {
            if (e.isSuccessful())
                storageRef.getDownloadUrl().addOnCompleteListener(callback);
        });
    }

    private void attachCallback(Task task, OnCompleteListener callback) {
        if (callback != null)
            task.addOnCompleteListener(callback);

    }
}
