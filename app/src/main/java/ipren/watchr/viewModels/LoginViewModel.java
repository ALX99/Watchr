package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.repository.IMainRepository;
import ipren.watchr.viewModels.util.*;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;

import ipren.watchr.repository.MainRepository;

public class LoginViewModel extends ViewModel {

    IMainRepository mainRepository;
    MutableLiveData<AuthenticationResponse> signInResponse = new MutableLiveData();
    MutableLiveData<AuthenticationResponse> createUserResponse = new MutableLiveData();
    LiveData<User> user;
    @VisibleForTesting
    public LoginViewModel(IMainRepository mainRepository){
        this.mainRepository = mainRepository;
        this.user = mainRepository.getUserLiveData();
    }

    public LoginViewModel(){
        this.mainRepository = MainRepository.getMainRepository();
        this.user = mainRepository.getUserLiveData();
    }

    public LiveData<User> getUser(){
        return this.user;
    }

    public LiveData<AuthenticationResponse> getSignInResponse(){
        return this.signInResponse;
    }

    public LiveData<AuthenticationResponse> getCreateUserResponse(){
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
        signInResponse.postValue(new AuthenticationResponse(task.isSuccessful(), error));
    }

    private void updateCreateUserResponse(Task task){
        String error = LoginErrorParser.parseAuthError(task.getException());
        createUserResponse.postValue(new AuthenticationResponse(task.isSuccessful(), error));
    }
}
