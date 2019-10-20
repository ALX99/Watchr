package ipren.watchr.viewModels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.util.LoginErrorParser;

public class LoginViewModel extends ViewModel {

    IUserDataRepository userDataRepository;
    MutableLiveData<AuthenticationResponse> signInResponse = new MutableLiveData();
    MutableLiveData<AuthenticationResponse> createUserResponse = new MutableLiveData();
    MutableLiveData<AuthenticationResponse> passwordResetResponse = new MutableLiveData<>();
    LiveData<User> user;

    @VisibleForTesting
    public LoginViewModel(IUserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
        this.user = userDataRepository.getUserLiveData();
    }

    public LoginViewModel() {
        this.userDataRepository = IUserDataRepository.getInstance();
        this.user = userDataRepository.getUserLiveData();
    }

    public LiveData<User> getUser() {
        return this.user;
    }

    public LiveData<AuthenticationResponse> getSignInResponse() {
        return this.signInResponse;
    }

    public LiveData<AuthenticationResponse> getCreateUserResponse() {
        return this.createUserResponse;
    }

    public LiveData<AuthenticationResponse> getPasswordResetResponse() {
        return this.passwordResetResponse;
    }

    public void registerUser(String email, String password) {
        userDataRepository.registerUser(email, password, this::updateCreateUserResponse);
    }

    public void signIn(String email, String password) {
        userDataRepository.loginUser(email, password, this::updateSignInResponse);
    }

    public void resetPassword(String email) {
        userDataRepository.resetPassword(email, this::updateResetPasswordResponse);
    }

    private void updateSignInResponse(Task task) {
        String error = LoginErrorParser.parseAuthError(task.getException());
        signInResponse.postValue(new AuthenticationResponse(task.isSuccessful(), error));
    }

    private void updateCreateUserResponse(Task task) {
        String error = LoginErrorParser.parseAuthError(task.getException());
        createUserResponse.postValue(new AuthenticationResponse(task.isSuccessful(), error));
    }

    private void updateResetPasswordResponse(Task task) {
        Exception exception = task.getException();
        passwordResetResponse.postValue(new AuthenticationResponse(task.isSuccessful(), exception != null ? exception.getLocalizedMessage() : "Unkown error"));
    }
}
