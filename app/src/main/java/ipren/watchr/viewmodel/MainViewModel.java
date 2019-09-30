package ipren.watchr.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ipren.watchr.model.User;


public class MainViewModel extends ViewModel implements MainViewModelInterface {

    //If you don't set a value the LiveData object wont broadcast any initial value, not even null
    //Setting user to null to signal that no user is logged in
    private MutableLiveData<User> user = new MutableLiveData<>(null);
    //Allows Activity to sync with viewmodel
    public LiveData<User> getUser(){
        return user;
    }


}
