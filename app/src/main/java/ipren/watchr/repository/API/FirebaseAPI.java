package ipren.watchr.repository.API;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import ipren.watchr.dataHolders.User;

public class FirebaseAPI {
    private FirebaseAuth mAuth;
    private final MutableLiveData user = new MutableLiveData(null);

    public FirebaseAPI() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(e -> {
                    FirebaseUser firebaseUser = e.getCurrentUser();
                    if (firebaseUser == null) {
                        this.user.postValue(firebaseUser);
                    } else {
                        String UID = firebaseUser.getUid();
                        Boolean isVerified = firebaseUser.isEmailVerified();
                        String email = firebaseUser.getEmail();
                        String userName = firebaseUser.getDisplayName();
                                this.user.postValue(new User(userName, email,UID, isVerified));
                    }
                }
        );


    }

    public LiveData<User> getUser() {
        return this.user;
    }
    public void loginUser(String email, String password, OnCompleteListener callback){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback);
    }
    public void registerUser(String email, String password, OnCompleteListener callback){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
            if(task.isSuccessful())
                task.getResult().getUser().sendEmailVerification();

           callback.onComplete(task);
        });
    }

    public void signOut(){
        mAuth.signOut();
    }
}
