package ipren.watchr.ViewModels;

import androidx.lifecycle.LiveData;

import ipren.watchr.DataHolders.User;

// This Interface enables us to swap MainActivity's ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();
}
