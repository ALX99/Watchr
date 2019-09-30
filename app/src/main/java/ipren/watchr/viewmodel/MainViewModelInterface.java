package ipren.watchr.viewmodel;

import androidx.lifecycle.LiveData;

import ipren.watchr.model.User;

// This Interface enables us to swap MainActivity's ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();
}
