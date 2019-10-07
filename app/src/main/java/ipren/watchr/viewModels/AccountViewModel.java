package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMainRepository;
import ipren.watchr.repository.MainRepository;

public class AccountViewModel extends ViewModel {

    LiveData<User> user;

    private IMainRepository mainRepository;
    @VisibleForTesting
    public AccountViewModel(IMainRepository iMainRepository){
        mainRepository = iMainRepository;
        this.user = mainRepository.getUserLiveData();
    }
    public AccountViewModel() {
        mainRepository = MainRepository.getMainRepository();
        this.user = mainRepository.getUserLiveData();
    }

    public void signOut(){
        mainRepository.signOutUser();
    }

    public LiveData<User> getUser(){
        return this.user;
    }
}
