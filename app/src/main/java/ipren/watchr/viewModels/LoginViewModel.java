package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ipren.watchr.viewModels.util.*;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.FireBaseResponse;
import ipren.watchr.dataHolders.User;

import ipren.watchr.repository.MainRepository;

public class LoginViewModel extends ViewModel {

    MainRepository mainRepository;
    MutableLiveData<FireBaseResponse> signInResponse = new MutableLiveData();
    MutableLiveData<FireBaseResponse> createUserResponse = new MutableLiveData();
    LiveData<User> user;

    public LoginViewModel(){
        this.mainRepository = MainRepository.getMainRepository();
        this.user = mainRepository.getUser();
    }

    public LiveData<User> getUser(){
        return this.user;
    }

    public LiveData<FireBaseResponse> getSignInResponse(){
        return this.signInResponse;
    }

    public LiveData<FireBaseResponse> getCreateUserResponse(){
        return this.createUserResponse;
    }

    public void registerUser(String email, String password){
        mainRepository.registerUser(email,password, res -> updateCreateUserResponse(res) );
    }

    public void signIn(String email, String password){
        mainRepository.loginUser(email, password, res -> updateSignInResponse(res));
    }

    private void updateSignInResponse(Task task){
        String error = LoginErrorParser.parseAuthError(task.getException());
        signInResponse.postValue(new FireBaseResponse(task.isSuccessful(), error));
    }

    private void updateCreateUserResponse(Task task){
        String error = LoginErrorParser.parseAuthError(task.getException());
        createUserResponse.postValue(new FireBaseResponse(task.isSuccessful(), error));
    }
}
