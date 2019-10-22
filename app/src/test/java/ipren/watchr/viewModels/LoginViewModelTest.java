package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.MockClasses.MockUserDataRepository;
import ipren.watchr.MockClasses.MockTaskResponse;
import ipren.watchr.dataHolders.RequestResponse;
import ipren.watchr.dataHolders.User;

import static org.junit.Assert.*;

//TODO remake this test
//@RunWith(RobolectricTestRunner.class)
//@Config(sdk = 28)
public class LoginViewModelTest {

    private LoginViewModel loginViewModel;
    private MockUserDataRepository mockUserDataRepository;
    private final User initialUser = new User("Fred", "Fred@test.com", null, null, false);
    @Before
    public void setUp(){
        mockUserDataRepository = new MockUserDataRepository(initialUser);
        loginViewModel = new LoginViewModel(mockUserDataRepository);
    }


    public void getUser() {
        User testUser = new User("Fred", "Fred@test.com1", null, null, false);
        LiveData<User> liveUser = loginViewModel.getUser();
        assertNotEquals(liveUser.getValue(), testUser);
        assertNotEquals(loginViewModel.getUser().getValue(), liveUser);
        mockUserDataRepository.setUser(testUser);
        assertEquals(liveUser.getValue(), testUser);
        assertEquals(loginViewModel.getUser().getValue(), testUser);
    }


    public void getSignInResponse() {
        LiveData<RequestResponse> authResLive = loginViewModel.getSignInResponse();
        MockTaskResponse mockTS= new MockTaskResponse();
        mockTS.setSuccessful(true);
        mockUserDataRepository.setAuthCallBack(mockTS);
        authResLive.observeForever(e -> {
            assertTrue(e.isSuccessful());
        });
        loginViewModel.signIn();
    }


    public void getRegisterUserResponse(){
        LiveData<RequestResponse> authResLive = loginViewModel.getCreateUserResponse();
        MockTaskResponse mockTS= new MockTaskResponse();
        mockTS.setSuccessful(true);
        mockUserDataRepository.setAuthCallBack(mockTS);
        authResLive.observeForever(e -> {
            assertTrue(e.isSuccessful());
        });
        loginViewModel.registerUser();
    }


    public void registerUser() {
        String testEmail = "fred@test.com";
        String testPassword = "123456";
        LiveData<User> userLiveData = loginViewModel.getUser();
        assertNotEquals(userLiveData.getValue().getEmail(), testEmail);
        loginViewModel.registerUser();
        assertEquals(testEmail, userLiveData.getValue().getEmail());
    }


    public void signIn() {
        String testEmail = "fred@test.com";
        String testPassword = "123456";
        LiveData<User> userLiveData = loginViewModel.getUser();
        assertNotEquals(userLiveData.getValue().getEmail(), testEmail);
        loginViewModel.signIn();
        assertEquals(testEmail, userLiveData.getValue().getEmail());

    }
}