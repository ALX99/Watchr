package ipren.watchr.viewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;


public class AccountSettingsViewModel extends ViewModel {

    IUserDataRepository userDataRepository;

    LiveData<User> liveUser;
    MutableLiveData<AuthenticationResponse> updateProfileResponse = new MutableLiveData<>();
    MutableLiveData<AuthenticationResponse> sendVerEmailResponse = new MutableLiveData<>();
    MutableLiveData<AuthenticationResponse> changePasswordResponse = new MutableLiveData<>();

   public AccountSettingsViewModel(){
       userDataRepository = IUserDataRepository.getInstance();
       this.liveUser = userDataRepository.getUserLiveData();

    }

    public LiveData<AuthenticationResponse> getUpdateProfileResponse(){
       return updateProfileResponse;
    }

    public LiveData<AuthenticationResponse> getVerificationResponse(){
       return  sendVerEmailResponse;
    }

    public LiveData<AuthenticationResponse> getChangePasswordResponse(){
       return changePasswordResponse;
    }

    public LiveData<User> getUser() {
        return liveUser;
    }

    public void refreshUsr() {
        userDataRepository.refreshUsr();
    }

    public void updateUserProfile(String username, Uri imageUri) {
        userDataRepository.updateProfile(username, imageUri, this::refreshUpdateUserProfile);
    }

    public void updateUserPassword(String oldPassword, String newPassword){
            userDataRepository.changePassword(oldPassword, newPassword, this::refreshPasswordChangeResponse);
    }

    public void resendVerificationEmail() {
        userDataRepository.reSendVerificationEmail(this::refreshEmailVerificationResponse);
    }

    private void refreshPasswordChangeResponse(Task task){
        Exception exception = task.getException();
        changePasswordResponse.postValue(new AuthenticationResponse(task.isSuccessful(),exception !=null ? exception.getMessage() : "" ));
    }

    private void refreshUpdateUserProfile(Task task){
        Exception exception = task.getException();
        updateProfileResponse.postValue(new AuthenticationResponse(task.isSuccessful(),exception !=null ? exception.getMessage() : "" ));
    }
    private void refreshEmailVerificationResponse(Task task){
        if(task == null){
            sendVerEmailResponse.postValue(new AuthenticationResponse(false, "Email Already verified"));
        }else{
            Exception exc = task.getException();
            sendVerEmailResponse.postValue(new AuthenticationResponse(task.isSuccessful(),exc !=null ? exc.getMessage() : "" ));
        }
    }




}
