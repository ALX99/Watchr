package ipren.watchr.viewmodels;

import android.os.Build;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.MockClasses.MockTaskResponse;
import ipren.watchr.MockClasses.UserDataRepositoryAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class LoginViewModelTest {
    final String failPW = "12345";
    final String passPW = "123456";
    final String failEmail = "failemail.com";
    final String passEmail = "valid@email.com";
    LoginViewModel loginViewModel;
    boolean methodInvoked = false;

    @Before
    public void setUp() {
        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter());
        methodInvoked = false;
    }

    @Test
    public void setRegEmailTxt() {
        assertNull(loginViewModel.regEmailError.getValue());
        loginViewModel.setRegEmailTxt("valid@email.com");
        assertEquals("valid@email.com", loginViewModel.getRegEmailTxt());
        assertNull(loginViewModel.regEmailError.getValue());
        loginViewModel.setRegEmailTxt("notValidemail.com");
        assertEquals("notValidemail.com", loginViewModel.getRegEmailTxt());
        assertNotNull(loginViewModel.regEmailError.getValue());
    }

    @Test
    public void setRegPasswordTxt() {
        assertNull(loginViewModel.regPasswordError.getValue());
        loginViewModel.setRegPasswordTxt("123456");
        assertEquals("123456", loginViewModel.getRegPasswordTxt());
        assertNull(loginViewModel.regPasswordError.getValue());

        loginViewModel.setRegPasswordTxt("1234");
        assertEquals("1234", loginViewModel.getRegPasswordTxt());
        assertNotNull(loginViewModel.regPasswordError.getValue());
    }

    @Test
    public void setRegReTypedPasswordTxt() {
        assertNull(loginViewModel.regReTypedPasswordError.getValue());
        loginViewModel.setRegPasswordTxt("12345678"); // This will set error to !=null because the reTyped field is empty
        assertNotNull(loginViewModel.regReTypedPasswordError.getValue());
        ((MutableLiveData<String>) loginViewModel.regReTypedPasswordError).postValue(null);  //Must reset null to test if setting identical PW will cause error
        loginViewModel.setRegReTypedPasswordTxt("12345678");
        assertEquals("12345678", loginViewModel.getRegReTypedPasswordTxt());
        assertNull(loginViewModel.regReTypedPasswordError.getValue());

        loginViewModel.setRegReTypedPasswordTxt("123456");
        assertEquals("123456", loginViewModel.getRegReTypedPasswordTxt());
        assertNotNull(loginViewModel.regReTypedPasswordError.getValue());
    }

    @Test
    public void setLogEmailTxt() {
        assertNull(loginViewModel.logEmailError.getValue());
        loginViewModel.setLogEmailTxt("valid@email.com");
        assertEquals("valid@email.com", loginViewModel.getLogEmailTxt());
        assertNull(loginViewModel.logEmailError.getValue());
        loginViewModel.setLogEmailTxt("notValidemail.com");
        assertEquals("notValidemail.com", loginViewModel.getLogEmailTxt());
        assertNotNull(loginViewModel.logEmailError.getValue());
    }

    @Test
    public void setLogPasswordTxt() {
        assertNull(loginViewModel.logPasswordError.getValue());
        loginViewModel.setLogPasswordTxt("123456");
        assertEquals("123456", loginViewModel.getLogPasswordTxt());
        assertNull(loginViewModel.logPasswordError.getValue());
        loginViewModel.setLogPasswordTxt("12345");
        assertEquals("12345", loginViewModel.getLogPasswordTxt());
        assertNotNull(loginViewModel.logPasswordError.getValue());
    }

    @Test
    public void setResetEmailTxt() {
        assertNull(loginViewModel.resetEmailError.getValue());
        loginViewModel.setResetEmailTxt("valid@email.com");
        assertEquals("valid@email.com", loginViewModel.getResetEmailTxt());
        assertNull(loginViewModel.resetEmailError.getValue());
        loginViewModel.setResetEmailTxt("notValidemail.com");
        assertEquals("notValidemail.com", loginViewModel.getResetEmailTxt());
        assertNotNull(loginViewModel.resetEmailError.getValue());
    }


    @Test
    public void getRegEmailTxt() {
        assertEquals("", loginViewModel.getRegEmailTxt());
    }

    @Test
    public void getRegPasswordTxt() {
        assertEquals("", loginViewModel.getRegPasswordTxt());
    }

    @Test
    public void getRegReTypedPasswordTxt() {
        assertEquals("", loginViewModel.getRegReTypedPasswordTxt());
    }

    @Test
    public void getLogEmailTxt() {
        assertEquals("", loginViewModel.getLogEmailTxt());
    }

    @Test
    public void getLogPasswordTxt() {
        assertEquals("", loginViewModel.getLogPasswordTxt());
    }

    @Test
    public void getResetEmailTxt() {
        assertEquals("", loginViewModel.getResetEmailTxt());
    }


    @Test
    public void registerUser() {


        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void registerUser(String email, String password, OnCompleteListener callback) {
                fail();
            }
        });
        assertFalse(loginViewModel.registerUser());
        assertNotNull(loginViewModel.regEmailError.getValue());
        assertNotNull(loginViewModel.regPasswordError.getValue());
        assertNotNull(loginViewModel.regReTypedPasswordError.getValue());

        testRegister(passEmail, passPW, failPW, loginViewModel, false);
        testRegister(passEmail, failPW, passPW, loginViewModel, false);
        testRegister(passEmail, failPW, failPW, loginViewModel, false);
        testRegister(failEmail, passPW, passPW, loginViewModel, false);
        testRegister(failEmail, passPW, failPW, loginViewModel, false);
        testRegister(failEmail, failPW, passPW, loginViewModel, false);
        testRegister(failEmail, failPW, failPW, loginViewModel, false);

        String s = loginViewModel.toString();
        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void registerUser(String email, String password, OnCompleteListener callback) {
                assertEquals(passEmail, email);
                assertEquals(passPW, password);
                assertNotNull(callback);
                assertTrue(loginViewModel.registeringUser.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(loginViewModel.registeringUser.getValue());
                methodInvoked = true;
            }
        });
        assertFalse(methodInvoked);
        testRegister(passEmail, passPW, passPW, loginViewModel, true);
        assertTrue(methodInvoked);
    }

    @Test
    public void signIn() {
        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void loginUser(String email, String password, OnCompleteListener callback) {
                fail();
            }
        });

        assertFalse(loginViewModel.signIn());
        assertNotNull(loginViewModel.logEmailError.getValue());
        assertNotNull(loginViewModel.logPasswordError.getValue());

        testSignIn(failEmail, failPW, loginViewModel, false);
        testSignIn(passEmail, failPW, loginViewModel, false);
        testSignIn(failEmail, passPW, loginViewModel, false);

        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void loginUser(String email, String password, OnCompleteListener callback) {
                assertEquals(passEmail, email);
                assertEquals(passPW, password);
                assertNotNull(callback);
                assertTrue(loginViewModel.signingIn.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(loginViewModel.signingIn.getValue());
                methodInvoked = true;
            }
        });
        assertFalse(methodInvoked);
        testSignIn(passEmail, passPW, loginViewModel, true);
        assertTrue(methodInvoked);


    }

    @Test
    public void resetPassword() {
        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void resetPassword(String email, OnCompleteListener callback) {
                fail();
            }
        });
        assertFalse(loginViewModel.resetPassword());
        assertNotNull(loginViewModel.resetEmailError.getValue());
        loginViewModel.setResetEmailTxt(failEmail);
        assertFalse(loginViewModel.resetPassword());


        loginViewModel = new LoginViewModel(new UserDataRepositoryAdapter() {
            @Override
            public void resetPassword(String email, OnCompleteListener callback) {
                assertTrue(loginViewModel.sendingResetMsg.getValue());
                assertEquals(passEmail, email);
                assertNotNull(callback);
                assertTrue(loginViewModel.sendingResetMsg.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(loginViewModel.sendingResetMsg.getValue());
                methodInvoked = true;
            }
        });

        loginViewModel.setResetEmailTxt(passEmail);
        assertFalse(methodInvoked);
        assertTrue(loginViewModel.resetPassword());
        assertTrue(methodInvoked);


    }

    private void testSignIn(String email, String pw, LoginViewModel loginViewModel, boolean exp) {
        loginViewModel.setLogEmailTxt(email);
        loginViewModel.setLogPasswordTxt(pw);
        assertEquals(exp, loginViewModel.signIn());
    }

    private void testRegister(String email, String pw, String reTypedPW, LoginViewModel loginViewModel, boolean exp) {
        loginViewModel.setRegEmailTxt(email);
        loginViewModel.setRegPasswordTxt(pw);
        loginViewModel.setRegReTypedPasswordTxt(reTypedPW);
        assertEquals(exp, loginViewModel.registerUser());
    }

}