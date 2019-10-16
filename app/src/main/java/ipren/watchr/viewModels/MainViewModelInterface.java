package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import ipren.watchr.dataHolders.User;

// This Interface enables us to swap MainActivity's ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();

}
