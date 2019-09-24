package ipren.watchr.ViewModels;

import androidx.lifecycle.LiveData;

import ipren.watchr.DataHolders.User;

// This Interface enables us to swap MainActivitys ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();
}
