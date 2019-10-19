package ipren.watchr.viewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;


public class AccountSettingsViewModel extends ViewModel {

    IUserDataRepository mainRepository;

    LiveData<User> liveUser;
    MutableLiveData<AuthenticationResponse> updateProfileResponse = new MutableLiveData<>();
    MutableLiveData<AuthenticationResponse> sendVerEmailResponse = new MutableLiveData<>();

   public AccountSettingsViewModel(){
       mainRepository = IUserDataRepository.getInstance();
       this.liveUser = mainRepository.getUserLiveData();

    }

    public LiveData<AuthenticationResponse> getUpdateProfileResponse(){
       return updateProfileResponse;
    }

    public LiveData<AuthenticationResponse> getVerificationResponse(){
       return  sendVerEmailResponse;
    }

    public LiveData<User> getUser() {
        return liveUser;
    }

    public void refreshUsr() {
        mainRepository.refreshUsr();
    }

    public void updateUserProfile(String username, Uri imageUri) {
        mainRepository.updateProfile(username, imageUri, this::refreshUpdateUserProfile);
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

    public void resendVerificationEmail() {
        mainRepository.reSendVerificationEmail(this::refreshEmailVerificationResponse);
    }


}
