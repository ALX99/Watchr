package ipren.watchr.repository;


import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.API.FirebaseAPI;

public class MainRepository implements IMainRepository{
    FirebaseAPI firebaseAPI;
    private static final IMainRepository MAIN_REPOSITORY = new MainRepository();
    public static IMainRepository getMainRepository(){
        return MAIN_REPOSITORY;
    }

    private  MainRepository(){
        firebaseAPI = new FirebaseAPI();
    }

    public LiveData<User> getUserLiveData(){
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
