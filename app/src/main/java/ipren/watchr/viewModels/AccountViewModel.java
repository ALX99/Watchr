package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.MainRepository;

public class AccountViewModel extends ViewModel {

    LiveData<User> user;

    private MainRepository mainRepository;
    public AccountViewModel() {
        mainRepository = MainRepository.getMainRepository();
        this.user = mainRepository.getUser();
    }

    public void signOut(){
        mainRepository.signOutUser();
    }

    public LiveData<User> getUser(){
        return this.user;
    }
}
