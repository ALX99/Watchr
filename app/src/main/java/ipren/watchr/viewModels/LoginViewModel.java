package ipren.watchr.viewModels;

import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.RequestResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.util.LoginErrorParser;

import static ipren.watchr.viewModels.util.ViewModelSupportUtils.*;

public class LoginViewModel extends ViewModel {

    public final IUserDataRepository userDataRepository;
    public final LiveData<RequestResponse> signInResponse = new MutableLiveData();
    public final LiveData<RequestResponse> createUserResponse = new MutableLiveData();
    public final LiveData<RequestResponse> passwordResetResponse = new MutableLiveData<>();
    public final LiveData<User> user;


    private String regEmailTxt = "";
    private String regPasswordTxt = "";
    private String regReTypedPasswordTxt = "";
    private String logEmailTxt = "";
    private String logPasswordTxt = "";
    private String resetEmailTxt = "";

    public final LiveData<Boolean> signingIn = new MutableLiveData<>(false);
    public final LiveData<Boolean> sendingResetMsg = new MutableLiveData<>(false);
    public final LiveData<Boolean> registeringUser = new MutableLiveData<>(false);

    public final LiveData<String> regEmailError = new MutableLiveData();
    public final LiveData<String> regPasswordError = new MutableLiveData();
    public final LiveData<String> regReTypedPasswordError = new MutableLiveData<>();
    public final LiveData<String> logEmailError = new MutableLiveData();
    public final LiveData<String> logPasswordError = new MutableLiveData();
    public final LiveData<String> resetEmailError = new MutableLiveData<>();


    @VisibleForTesting
    public LoginViewModel(IUserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
        this.user = userDataRepository.getUserLiveData();
    }

    public LoginViewModel() {
        this.userDataRepository = IUserDataRepository.getInstance();
        this.user = userDataRepository.getUserLiveData();
    }

    public boolean registerUser() {
        if (regEmailTxt.isEmpty() || regPasswordTxt.isEmpty()) {
            if (regEmailTxt.isEmpty()) postValue(regEmailError, "Please enter your email");
            if (regPasswordTxt.isEmpty()) {
                postValue(regReTypedPasswordError,"Please re-enter password" );
                postValue(regPasswordError, "Please enter a password");
            }
            return false;
        } else if (isEmailFormat(regEmailTxt) && regPasswordTxt.length() > 5 && regPasswordTxt.equals(regReTypedPasswordTxt)) {
            postValue(registeringUser, true);
            userDataRepository.registerUser(regEmailTxt, regPasswordTxt, this::updateCreateUserResponse);
            return true;
        } else
            return false;
    }

    public boolean signIn() {
        if (logEmailTxt.isEmpty() || logPasswordTxt.isEmpty()) {
            if (logEmailTxt.isEmpty()) postValue(logEmailError, "Please write your email");
            if (logPasswordTxt.isEmpty()) postValue(logPasswordError, "Please write a password");
            return false;
        } else if (!isEmailFormat(logEmailTxt) || logPasswordTxt.length() < 6) {
            return false;
        } else {
            postValue(signingIn, true);
            userDataRepository.loginUser(logEmailTxt, logPasswordTxt, this::updateSignInResponse);
            return true;
        }

    }

    public boolean resetPassword() {
        if (resetEmailTxt.isEmpty()) {
            postValue(resetEmailError, "Please write your email");
            return false;
        } else if (isEmailFormat(resetEmailTxt)) {
            postValue(sendingResetMsg, true);
            userDataRepository.resetPassword(resetEmailTxt, this::updateResetPasswordResponse);
            return true;
        } else
            return false;
    }

    private void updateSignInResponse(Task task) {
        postValue(signingIn, false);
        String error = LoginErrorParser.parseAuthError(task.getException());
        postValue(signInResponse, new RequestResponse(task.isSuccessful(), error));
    }

    private void updateCreateUserResponse(Task task) {
        postValue(registeringUser, false);
        String error = LoginErrorParser.parseAuthError(task.getException());
        postValue(createUserResponse, new RequestResponse(task.isSuccessful(), error));
    }

    private void updateResetPasswordResponse(Task task) {
        postValue(sendingResetMsg, false);
        Exception exception = task.getException();
        postValue(passwordResetResponse, new RequestResponse(task.isSuccessful(), exception != null ? exception.getLocalizedMessage() : "Unkown error"));
    }

    private <T> void postValue(LiveData<T> liveData, T object) {
        try {
            ((MutableLiveData<T>) liveData).postValue(object);
        } catch (Exception e) {
            Log.e("Watchr", "unable to post value");
        }

    }

    public void setRegEmailTxt(String regEmailTxt) {
        this.regEmailTxt = regEmailTxt;
        updateEmailErrorTxt(regEmailError, regEmailTxt);
    }

    public void setRegPasswordTxt(String regPasswordTxt) {
        this.regPasswordTxt = regPasswordTxt;
        updatePasswordErrorTxt(regPasswordError, regPasswordTxt, 6);
    }

    public void setRegReTypedPasswordTxt(String regReTypedPasswordTxt) {
        this.regReTypedPasswordTxt = regReTypedPasswordTxt;
        updatePasswordMatchErrorTxt(regReTypedPasswordError, regPasswordTxt, regReTypedPasswordTxt);
    }

    public void setLogEmailTxt(String logEmailTxt) {
        this.logEmailTxt = logEmailTxt;
        updateEmailErrorTxt(logEmailError, logEmailTxt);
    }

    public void setLogPasswordTxt(String logPasswordTxt) {
        this.logPasswordTxt = logPasswordTxt;
        updatePasswordErrorTxt(logPasswordError, logPasswordTxt,6);
    }

    public void setResetEmailTxt(String resetEmailTxt) {
        this.resetEmailTxt = resetEmailTxt;
        updateEmailErrorTxt(resetEmailError, resetEmailTxt);
    }


    // Because java.
    public String getRegEmailTxt() {
        return regEmailTxt;
    }

    public String getRegPasswordTxt() {
        return regPasswordTxt;
    }

    public String getRegReTypedPasswordTxt() {
        return regReTypedPasswordTxt;
    }

    public String getLogEmailTxt() {
        return logEmailTxt;
    }

    public String getLogPasswordTxt() {
        return logPasswordTxt;
    }

    public String getResetEmailTxt() {
        return resetEmailTxt;
    }


}
