package ipren.watchr.viewmodels;

import androidx.lifecycle.LiveData;

import ipren.watchr.dataholders.User;

// This Interface enables us to swap MainActivity's ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();

}
