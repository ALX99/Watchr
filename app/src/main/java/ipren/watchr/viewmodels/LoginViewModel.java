package ipren.watchr.viewmodels;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataholders.RequestResponse;
import ipren.watchr.dataholders.User;
import ipren.watchr.repository.IUserDataRepository;

import static ipren.watchr.viewmodels.util.ErrorParser.parseError;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.isEmailFormat;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.postValue;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updateEmailErrorTxt;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updatePasswordErrorTxt;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updatePasswordMatchErrorTxt;

public class LoginViewModel extends ViewModel {

    private final IUserDataRepository userDataRepository; // For fetching and updating the signed in users data
    private final int minPasswordLength = 6; //Minimum password length in fore fireBase Auth API

    public final LiveData<User> user; //The currently signed in user

    public final LiveData<RequestResponse> signInResponse = new MutableLiveData(); //This variable represents response from an attempt to sign in
    public final LiveData<RequestResponse> registerUserResponse = new MutableLiveData(); //This variable represents response from an attempt to register a user
    public final LiveData<RequestResponse> passwordResetResponse = new MutableLiveData<>(); //This variable represents response from an attempt to send a password reset email

    public final LiveData<Boolean> signingIn = new MutableLiveData<>(false); //These variables mirror the state of certain operations
    public final LiveData<Boolean> sendingResetMsg = new MutableLiveData<>(false);
    public final LiveData<Boolean> registeringUser = new MutableLiveData<>(false);


    public final LiveData<String> regEmailError = new MutableLiveData(); //This variable shows weather or not the email enterd at registration follows email format
    public final LiveData<String> regPasswordError = new MutableLiveData(); //This variable shows weather or not  the entered password at registration is correctly formatted
    public final LiveData<String> regReTypedPasswordError = new MutableLiveData<>(); //This variable shows weather or not the retyped password at registration matches the entered password
    public final LiveData<String> logEmailError = new MutableLiveData(); //This variable shows weather or not the email entered at login follows email format
    public final LiveData<String> logPasswordError = new MutableLiveData(); // This variable shows weather or not the password entered at login is correctly formatted
    public final LiveData<String> resetEmailError = new MutableLiveData<>(); // This variable shows weather or not the email entered at reset passwod follows email format

    //These variables each represent a text field,
    private String regEmailTxt = "";
    private String regPasswordTxt = "";
    private String regReTypedPasswordTxt = "";
    private String logEmailTxt = "";
    private String logPasswordTxt = "";
    private String resetEmailTxt = "";


    @VisibleForTesting
    public LoginViewModel(IUserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
        this.user = userDataRepository.getUserLiveData();
    }
    //Default constructor used by ViewModelProviders
    public LoginViewModel() {
        this.userDataRepository = IUserDataRepository.getInstance();
        this.user = userDataRepository.getUserLiveData();
    }

    //Handles attempts to register a user, will reject/pass attempt based on set values, If it passes it will set the state of "registeringUser" to true and attempt to register with the set values, wil reset once callback is triggered
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
    //Handles attempts to register a user, will reject/pass attempt based on set values, If it passes it will set the state of "signingIN" to true and attempt to sigIn with the set values, wil reset once callback is triggered
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
    //Handles attempts to reset password, will reject/pass attempt based weather or not the set email is properly formatted, If it passes it will set the state of "sendingResetMsg" to true and attempt to send reset password email, wil reset once callback is triggered
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
    //Callback function, posts the response to corresponding LiveData and sets the state of "signIn" to false.
    private void updateSignInResponse(Task task) {
        postValue(signingIn, false);
        postValue(signInResponse, new RequestResponse(task.isSuccessful(), parseError(task.getException())));
    }
    //Callback function, posts the response  to corresponding LiveData and sets the state of "registeringUser" to false.
    private void updateCreateUserResponse(Task task) {
        postValue(registeringUser, false);
        postValue(registerUserResponse, new RequestResponse(task.isSuccessful(), parseError(task.getException())));
    }
    //Callback function, posts the response to corresponding LiveData  and sets the state of "sendingResetMSg" to false.
    private void updateResetPasswordResponse(Task task) {
        postValue(sendingResetMsg, false);
        postValue(passwordResetResponse, new RequestResponse(task.isSuccessful(), parseError(task.getException())));
    }

    // The following setters update the mirrored value in the viewModel so that it can be used for logic and syncing activity fields in the event of a reconstruction. It also sets a corresponding error if the value is invalid
    public void setRegEmailTxt(String regEmailTxt) {
        this.regEmailTxt = regEmailTxt;
        updateEmailErrorTxt(regEmailError, regEmailTxt);
    }

    public void setRegPasswordTxt(String regPasswordTxt) {
        this.regPasswordTxt = regPasswordTxt;
        updatePasswordErrorTxt(regPasswordError, regPasswordTxt, minPasswordLength);
        updatePasswordMatchErrorTxt(regReTypedPasswordError, regPasswordTxt, regReTypedPasswordTxt);
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
        updatePasswordErrorTxt(logPasswordError, logPasswordTxt,minPasswordLength);
    }

    public void setResetEmailTxt(String resetEmailTxt) {
        this.resetEmailTxt = resetEmailTxt;
        updateEmailErrorTxt(resetEmailError, resetEmailTxt);
    }


    //These fields are currently not used in any activity because we are only using a vertical layout, and activity reconstruction wont happen unless its killed by android.
    //Normally you'd use them to sync the activity data after a reconstruction
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
