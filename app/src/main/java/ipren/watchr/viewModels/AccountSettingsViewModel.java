package ipren.watchr.viewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;


public class AccountSettingsViewModel extends ViewModel {

    IUserDataRepository mainRepository;

    LiveData<User> liveUser;

    MutableLiveData<AuthenticationResponse> sendVerEmailResponse = new MutableLiveData<>(null);

    public AccountSettingsViewModel() {
        mainRepository = IUserDataRepository.getInstance();
        this.liveUser = mainRepository.getUserLiveData();

    }

    public LiveData<AuthenticationResponse> getVerificationResponse() {
        return sendVerEmailResponse;
    }

    public LiveData<User> getUser() {
        return liveUser;
    }

    public void refreshUsr() {
        mainRepository.refreshUsr();
    }

    public void updateUserProfile(String username, Uri imageUri) {
        mainRepository.updateProfile(username, imageUri);
    }

    public void resendVerificationEmail() {
        mainRepository.reSendVerificationEmail(e -> {
            if (e == null) {
                sendVerEmailResponse.postValue(new AuthenticationResponse(false, "Email Already verified"));
            } else {
                Exception exc = e.getException();
                sendVerEmailResponse.postValue(new AuthenticationResponse(e.isSuccessful(), exc != null ? exc.getMessage() : ""));
            }
        });
    }


}
