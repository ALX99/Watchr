package ipren.watchr.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;


public class MainViewModel extends ViewModel implements MainViewModelInterface {

    private final IUserDataRepository mainRepository;
    //If you don't set a value the LiveData object wont broadcast any initial value, not even null
    //Setting user to null to signal that no user is logged in

    private LiveData<User> user;

    public MainViewModel(IUserDataRepository mainRepository) {
        this.mainRepository = mainRepository;
        user = mainRepository.getUserLiveData();
    }

    public MainViewModel() {
        mainRepository = IUserDataRepository.getInstance();
        user = mainRepository.getUserLiveData();
    }

    //Allows Activity to sync with viewmodel
    public LiveData<User> getUser() {
        return user;
    }


    /*
    Probably even more stuff to add later, such as a table to contain liked movies of different users,
    being able to delete comments, watched movies etc etc, but thats probably better to take on later.
    */
}
