package ipren.watchr.repository;


import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.FirebaseAPI;

public class MainRepository {
    private static final MainRepository MAIN_REPOSITORY = new MainRepository();
    public static MainRepository getMainRepository(){
        return MAIN_REPOSITORY;
    }
    FirebaseAPI firebaseAPI;
    private  MainRepository(){
        firebaseAPI = new FirebaseAPI();
    }

    public LiveData<User> getUser(){
        return firebaseAPI.getUser();
    }

    public void registerUser(String email, String password, OnCompleteListener callback){
        firebaseAPI.registerUser(email,password, callback);
    }

    public void signOutUser(){
       firebaseAPI.signOut();
    }

    public void loginUser(String email, String password, OnCompleteListener callback){
        firebaseAPI.loginUser(email, password, callback);
    }
}
