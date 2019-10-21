package ipren.watchr.viewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.RequestResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import static ipren.watchr.viewModels.util.ViewModelSupportUtils.*;


public class AccountSettingsViewModel extends ViewModel {

    public void setNewProfilePicture(Uri newProfilePicture) {
        this.newProfilePicture = newProfilePicture;
    }

    private String oldPassword = "";
    private String newPassword = "";
    private String reTypedPassword = "";
    private String username = "";
    private Uri newProfilePicture;



    private IUserDataRepository userDataRepository;
    public final LiveData<User> liveUser;
    public final LiveData<RequestResponse> updateProfileResponse = new MutableLiveData<>();
    public final LiveData<RequestResponse> sendVerEmailResponse = new MutableLiveData<>();
    public final LiveData<RequestResponse> changePasswordResponse = new MutableLiveData<>();

    public final LiveData<String> oldPasswordErrorTxt = new MutableLiveData<>();
    public final LiveData<String> newPasswordErrorTxt = new MutableLiveData<>();
    public final LiveData<String> reTypedErrorTxt = new MutableLiveData<>();
    public final LiveData<String> usernameErrorTxt = new MutableLiveData<>();

    public final LiveData<Boolean> savingPublicProfile = new MutableLiveData<>();
    public final LiveData<Boolean> changingPassword = new MutableLiveData<>();
    public final LiveData<Boolean> sendingVerificationEmail = new MutableLiveData<>();
    public final LiveData<Boolean> checkingUserVerification = new MutableLiveData<>();


   public AccountSettingsViewModel(){
       userDataRepository = IUserDataRepository.getInstance();
       this.liveUser = userDataRepository.getUserLiveData();
    }

    public LiveData<RequestResponse> getUpdateProfileResponse(){
       return updateProfileResponse;
    }

    public LiveData<RequestResponse> getVerificationResponse(){
       return  sendVerEmailResponse;
    }

    public LiveData<RequestResponse> getChangePasswordResponse(){
       return changePasswordResponse;
    }

    public LiveData<User> getUser() {
        return liveUser;
    }

    public void refreshUsr() {
        userDataRepository.refreshUsr();
    }

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
           postValue(savingPublicProfile, true);
           userDataRepository.updateProfile(username, newProfilePicture, this::refreshUpdateUserProfile);
           return true;
       }
    }

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

    public void resendVerificationEmail() {
        postValue(sendingVerificationEmail, true);
        userDataRepository.reSendVerificationEmail(this::refreshEmailVerificationResponse);
    }

    private void refreshPasswordChangeResponse(Task task){
       postValue(changingPassword, false);
        Exception exception = task.getException();
        postValue(changePasswordResponse, new RequestResponse(task.isSuccessful(),exception !=null ? exception.getMessage() : "" ));
    }

    private void refreshUpdateUserProfile(Task task){
       postValue(savingPublicProfile, false);
        Exception exception = task.getException();
        postValue(updateProfileResponse ,new RequestResponse(task.isSuccessful(),exception !=null ? exception.getMessage() : "" ));
    }
    private void refreshEmailVerificationResponse(Task task){
        if(task == null){
            postValue(sendVerEmailResponse, new RequestResponse(false, "Email Already verified"));
        }else{
            Exception exc = task.getException();
            postValue(sendVerEmailResponse,new RequestResponse(task.isSuccessful(),exc !=null ? exc.getMessage() : "" ));
        }
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        updatePasswordErrorTxt(oldPasswordErrorTxt,oldPassword);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        updatePasswordErrorTxt(newPasswordErrorTxt,newPassword);
    }

    public void setReTypedPassword(String reTypedPassword) {
        this.reTypedPassword = reTypedPassword;
        updatePasswordMatchErrorTxt(reTypedErrorTxt, newPassword, reTypedPassword);
    }

    public void setUsername(String username) {
        this.username = username;
        setStringTooLongErroTxt(usernameErrorTxt, username, 15);
    }


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
