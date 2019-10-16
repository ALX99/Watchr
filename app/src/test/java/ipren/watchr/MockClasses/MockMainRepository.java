package ipren.watchr.MockClasses;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
//TODO remake this test

public class MockMainRepository implements IUserDataRepository {

    private Task authResponse;
    /**
     *
     * @param user Initial user
     */
    public MockMainRepository(User user){
       userLiveData = new MutableLiveData<>(user);
       MockTaskResponse mockTaskResponse = new MockTaskResponse();
       mockTaskResponse.setSuccessful(false);
       authResponse = mockTaskResponse;
    }

    public MockMainRepository(){}

    MutableLiveData<User> userLiveData = new MutableLiveData<>(null);

    public void setUser(User user){
        this.userLiveData.postValue(user);
    }

    public void setAuthCallBack(Task callbackRes){
        authResponse = callbackRes;
    }
    @Override
    public LiveData<User> getUserLiveData() {
        return this.userLiveData;
    }

    @Override
    public void registerUser(String email, String password, OnCompleteListener callback) {
        this.userLiveData.postValue(new User("unkown",email, null, null, false));
        callback.onComplete(authResponse);
    }

    @Override
    public void signOutUser() {
        this.userLiveData.postValue(null);
    }

    @Override
    public void loginUser(String email, String password, OnCompleteListener callback) {
        this.userLiveData.postValue(new User("unkown",email, null, null, false));
        callback.onComplete(authResponse);
    }

    @Override
    public void refreshUsr() {

    }

    @Override
    public void reSendVerificationEmail() {

    }

    @Override
    public void updateProfile(String userName, Uri pictureUri) {

    }
}