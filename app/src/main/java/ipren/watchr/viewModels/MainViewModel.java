package ipren.watchr.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMainRepository;
import ipren.watchr.repository.MainRepository;


public class MainViewModel extends ViewModel implements MainViewModelInterface {

    private final IMainRepository mainRepository;
    //If you don't set a value the LiveData object wont broadcast any initial value, not even null
    //Setting user to null to signal that no user is logged in
    private LiveData<User>user;
    //Allows Activity to sync with viewmodel
    public LiveData<User> getUser(){
        return user;
    }

    public MainViewModel(IMainRepository mainRepository){
        this.mainRepository = mainRepository;
        user = mainRepository.getUserLiveData();
    }
    public MainViewModel(){
        mainRepository = MainRepository.getMainRepository();
        user = mainRepository.getUserLiveData();
    }
}
