package ipren.watchr.viewModels;


import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ipren.watchr.dataHolders.User;


public class MainViewModel extends ViewModel implements MainViewModelInterface {
    private User mockUser = new User("David Olsson","david@ipren.com");
    //If you don't set a value the LiveData object wont broadcast any initial value, not even null
    //Setting user to null to signal that no user is logged in
    private MutableLiveData<User> user = new MutableLiveData<>(null);
    //Allows Activity to sync with viewmodel
    public LiveData<User> getUser(){
        return user;
    }



    //This method is loaded with dummy values untill the repository is connected;
    public boolean isEmailRegistered(String email){
        return email.equalsIgnoreCase(mockUser.getEmail());
    }

    //This method is loaded with dummy values untill the repository is connected;
    public boolean loginUser(String email, String password){
        if(email.equalsIgnoreCase(mockUser.getEmail()) && password.equalsIgnoreCase("123456")){
            user.postValue(mockUser);
            return true;
        }
        return false;
    }

    public void logoutCurrentUser(){
        this.user.postValue(null);
    }

}
