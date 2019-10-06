package ipren.watchr.repository;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import ipren.watchr.dataHolders.User;

public interface IMainRepository {

     LiveData<User> getUserLiveData();

     void registerUser(String email, String password, OnCompleteListener callback);

     void signOutUser();

     void loginUser(String email, String password, OnCompleteListener callback);

}
