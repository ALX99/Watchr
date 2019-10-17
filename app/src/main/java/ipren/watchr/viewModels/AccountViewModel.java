package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;

public class AccountViewModel extends ViewModel {

    LiveData<User> user;

    private IUserDataRepository mainRepository;

    @VisibleForTesting
    public AccountViewModel(IUserDataRepository iMainRepository) {
        mainRepository = iMainRepository;
        this.user = mainRepository.getUserLiveData();
    }

    public AccountViewModel() {
        mainRepository = IUserDataRepository.getInstance();
        this.user = mainRepository.getUserLiveData();
    }

    public void signOut() {
        mainRepository.signOutUser();
    }

    public LiveData<User> getUser() {
        return this.user;
    }
}
