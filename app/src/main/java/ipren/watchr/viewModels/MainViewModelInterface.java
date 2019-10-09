package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.User;

// This Interface enables us to swap MainActivity's ViewModel
public interface MainViewModelInterface {
    LiveData<User> getUser();
    LiveData<User> getUser(int UID);
    List<Comment> getCommentsFromMovie();
}
