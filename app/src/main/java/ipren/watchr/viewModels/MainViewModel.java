package ipren.watchr.viewModels;


import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ipren.watchr.dataHolders.User;


public class MainViewModel extends ViewModel implements MainViewModelInterface {

    //If you don't set a value the LiveData object wont broadcast any initial value, not even null
    //Setting user to null to signal that no user is logged in
    private MutableLiveData<User> user = new MutableLiveData<>(null);
    //Allows Activity to sync with viewmodel
    public LiveData<User> getUser(){
        return user;
    }

    //This method is loaded with dummy values untill the repository is connected;
    public boolean validUser(String email){
        return email.equalsIgnoreCase("david@ipren.com");
    }

    //This method is loaded with dummy values untill the repository is connected;
    public boolean loginUser(String email, String password){
        if(email.equalsIgnoreCase("david@ipren.com") && password.equalsIgnoreCase("123456")){
            user.postValue(new User("David Olsson"));
            return true;
        }
        return false;
    }

}
