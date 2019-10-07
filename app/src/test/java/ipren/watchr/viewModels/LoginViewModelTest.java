package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.MockClasses.MockMainRepository;
import ipren.watchr.MockClasses.MockTaskResponse;
import ipren.watchr.dataHolders.AuthenticationResponse;
import ipren.watchr.dataHolders.User;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class LoginViewModelTest {

    private LoginViewModel loginViewModel;
    private MockMainRepository mockMainRepository;
    private final User initialUser = new User("David", "david@ipren.com");
    @Before
    public void setUp(){
        mockMainRepository = new MockMainRepository(initialUser);
        loginViewModel = new LoginViewModel(mockMainRepository);
    }

    @Test
    public void getUser() {
        User testUser = new User("Test", "test@ipren.com1");
        LiveData<User> liveUser = loginViewModel.getUser();
        assertNotEquals(liveUser.getValue(), testUser);
        assertNotEquals(loginViewModel.getUser().getValue(), liveUser);
        mockMainRepository.setUser(testUser);
        assertEquals(liveUser.getValue(), testUser);
        assertEquals(loginViewModel.getUser().getValue(), testUser);
    }

    @Test
    public void getSignInResponse() {
        LiveData<AuthenticationResponse> authResLive = loginViewModel.getSignInResponse();
        MockTaskResponse mockTS= new MockTaskResponse();
        mockTS.setSuccessful(true);
        mockMainRepository.setAuthCallBack(mockTS);
        authResLive.observeForever(e -> {
            assertTrue(e.isSuccessful());
        });
        loginViewModel.signIn("not@Used.com", "123456");
    }

    @Test
    public void getRegisterUserResponse(){
        LiveData<AuthenticationResponse> authResLive = loginViewModel.getCreateUserResponse();
        MockTaskResponse mockTS= new MockTaskResponse();
        mockTS.setSuccessful(true);
        mockMainRepository.setAuthCallBack(mockTS);
        authResLive.observeForever(e -> {
            assertTrue(e.isSuccessful());
        });
        loginViewModel.registerUser("not@Used.com", "123456");
    }

    @Test
    public void registerUser() {
        String testEmail = "fred@ipren.com";
        String testPassword = "123456";
        LiveData<User> userLiveData = loginViewModel.getUser();
        assertNotEquals(userLiveData.getValue().getEmail(), testEmail);
        loginViewModel.registerUser(testEmail, testPassword);
        assertEquals(testEmail, userLiveData.getValue().getEmail());
    }

    @Test
    public void signIn() {
        String testEmail = "fred@ipren.com";
        String testPassword = "123456";
        LiveData<User> userLiveData = loginViewModel.getUser();
        assertNotEquals(userLiveData.getValue().getEmail(), testEmail);
        loginViewModel.signIn(testEmail, testPassword);
        assertEquals(testEmail, userLiveData.getValue().getEmail());

    }
}