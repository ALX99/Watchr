package ipren.watchr.viewModels;

import android.net.Uri;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.RequestResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import static ipren.watchr.viewModels.util.ViewModelSupportUtils.*;
import static ipren.watchr.viewModels.util.ErrorParser.*;


public class AccountSettingsViewModel extends ViewModel {

    private final int minPasswordLength = 6; //Minimum password length in fore firebase Auth API
    private final IUserDataRepository userDataRepository; // For fetching and updating the signed in users data

    public final LiveData<User> liveUser; //The currently signed in user

    public final LiveData<RequestResponse> updateProfileResponse = new MutableLiveData<>(); //This variable represents response from an attempt to update profile
    public final LiveData<RequestResponse> sendVerEmailResponse = new MutableLiveData<>(); // This variable represents response from an attempt to send  a email verification
    public final LiveData<RequestResponse> changePasswordResponse = new MutableLiveData<>(); //This variable represents a response from an attempt to change the current signed in users password

    public final LiveData<Boolean> savingNewProfile = new MutableLiveData<>(); //These variables mirror the state of certain operations
    public final LiveData<Boolean> changingPassword = new MutableLiveData<>();
    public final LiveData<Boolean> sendingVerificationEmail = new MutableLiveData<>();

    public final LiveData<String> oldPasswordErrorTxt = new MutableLiveData<>(); //This variable shows weather or not the old password text field is properly formatted
    public final LiveData<String> newPasswordErrorTxt = new MutableLiveData<>(); //This variable shows weather or not the new password text field is properly formatted
    public final LiveData<String> reTypedErrorTxt = new MutableLiveData<>(); //This variable shows weather or not the new password matches the reTyped password
    public final LiveData<String> usernameErrorTxt = new MutableLiveData<>(); //This field shows if the username is to long

    //These variables each represent a text field,
    private String oldPassword = "";
    private String newPassword = "";
    private String reTypedPassword = "";
    private String username = "";
    private Uri newProfilePicture;


    @VisibleForTesting
    public AccountSettingsViewModel(IUserDataRepository userDataRepository){
        this.userDataRepository = userDataRepository;
        this.liveUser = userDataRepository.getUserLiveData();
    }

    //Default constructor used by ViewModelProviders
    public AccountSettingsViewModel(){
       userDataRepository = IUserDataRepository.getInstance();
       this.liveUser = userDataRepository.getUserLiveData();
    }

    //Refreshes the user, triggering Livedata<User> with a newly fetched user
    public void refreshUsr() {
        userDataRepository.refreshUsr();
    }

    //Handles attempts to update the user profile, will reject/pass attempt based on set values, username(txt) newProfilePicture(uri). If the test passes it will set state of "savingNewProfile" to true and attempt to update profile with set values, will reset once callback is triggered
    public boolean updateUserProfile() {
       if(username.isEmpty()){
            postValue(usernameErrorTxt, "Username please");
           return false;
       }else if (username.equals(liveUser.getValue().getUserName()) && newProfilePicture == null){
            return false;
       } else if (username.length() > 15){
           return false;
       }
       else {
           postValue(savingNewProfile, true);
           userDataRepository.updateProfile(username, newProfilePicture, this::refreshUpdateUserProfile);
           return true;
       }
    }
    //Handles attempts to update userpassword, will reject/pass attempt based on set values, password fields(txt). If the test passes it will set state of "changingPassword" to true and attempt to update profile with set values, will reset once callback is triggered
    public boolean updateUserPassword(){
       if(oldPassword.isEmpty() || newPassword.isEmpty()){
           if(oldPassword.isEmpty()) postValue(oldPasswordErrorTxt, "Please enter your current password");
           if(newPassword.isEmpty()) {
               postValue(newPasswordErrorTxt, "Please enter a password");
                postValue(reTypedErrorTxt,"Please re-enter password"  );
           }
           return false;
       }else if(oldPassword.length() > 5 && newPassword.length() > 5 && newPassword.equals(reTypedPassword)){
           postValue(changingPassword, true);
           userDataRepository.changePassword(oldPassword, newPassword, this::refreshPasswordChangeResponse);
           return true;
       }
       return false;

    }

    //Attempts to send an email verification, sets the state of "sendingVerificationEmail" to true, will reset once callback is triggerd
    public void resendVerificationEmail() {
        postValue(sendingVerificationEmail, true);
        userDataRepository.reSendVerificationEmail(this::refreshEmailVerificationResponse);
    }
    //Callback function, posts the response  to corresponding LiveData and sets the state of "sendingVerificationEmail" to false.
    private void refreshEmailVerificationResponse(Task task){
        postValue(sendingVerificationEmail, false);
        if(task == null){
            postValue(sendVerEmailResponse, new RequestResponse(false, "Email Already verified"));
        }else{
            postValue(sendVerEmailResponse,new RequestResponse(task.isSuccessful(), parseError(task.getException())));
        }
    }

    //Callback function, posts the response to corresponding LiveData and sets the state of "changingPassword" to false;
    private void refreshPasswordChangeResponse(Task task){
       postValue(changingPassword, false);
        postValue(changePasswordResponse, new RequestResponse(task.isSuccessful(), parseError(task.getException())));
    }
    //Callback function, posts the response to corresponding LiveData and sets the state of "savingNewProfile" to false;
    private void refreshUpdateUserProfile(Task task){
       postValue(savingNewProfile, false);
       if(task.isSuccessful())
           newProfilePicture = null;
        postValue(updateProfileResponse ,new RequestResponse(task.isSuccessful(), parseError(task.getException())));
    }

    // The following setters update the mirrored value in the viewModel so that it can be used for logic and syncing activity fields in the event of a reconstruction. It also sets a corresponding error if the value is invalid
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        updatePasswordErrorTxt(oldPasswordErrorTxt,oldPassword, minPasswordLength);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        updatePasswordErrorTxt(newPasswordErrorTxt,newPassword, minPasswordLength);
        updatePasswordMatchErrorTxt(reTypedErrorTxt, newPassword, reTypedPassword);
    }

    public void setReTypedPassword(String reTypedPassword) {
        this.reTypedPassword = reTypedPassword;
        updatePasswordMatchErrorTxt(reTypedErrorTxt, newPassword, reTypedPassword);
    }

    public void setUsername(String username) {
        this.username = username;
        setStringTooLongErrorTxt(usernameErrorTxt, username, 15);
    }

    public void setNewProfilePicture(Uri newProfilePicture) {
        this.newProfilePicture = newProfilePicture;
    }


    //These fields are currently not used in any activity because we are only using a vertical layout, and activity reconstruction wont happen unless its killed by android.
    //Normally you'd use them to sync the activity data after a reconstruction
    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getReTypedPassword() {
        return reTypedPassword;
    }

    public String getUsername() {
        return username;
    }

    public Uri getNewProfilePicture() {
        return newProfilePicture;
    }






}
