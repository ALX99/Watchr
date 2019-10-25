package ipren.watchr.viewModels;

import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.BuildConfig;
import ipren.watchr.MockClasses.MockTaskResponse;
import ipren.watchr.MockClasses.UserDataRepositoryAdapter;
import ipren.watchr.R;
import ipren.watchr.dataHolders.User;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class AccountSettingsViewModelTest {
    AccountSettingsViewModel settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter());
    boolean methodInvoked = false;

    String testUsername;
    Uri testURi;
    @Before
    public void setUp(){
        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter());
        methodInvoked = false;
        testUsername = null;
        testURi = null;


    }

    @Test
    public void setNewProfilePicture() {
        Uri testURI = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
        settingsViewModel.setNewProfilePicture(testURI);
        assertEquals(testURI, settingsViewModel.getNewProfilePicture());
    }
    @Test
    public void getNewProfilePicture() {
        assertNull(settingsViewModel.getNewProfilePicture());
    }

    @Test
    public void setOldPassword() {
       assertNull(settingsViewModel.oldPasswordErrorTxt.getValue());
       settingsViewModel.setOldPassword("123456");
       assertEquals("123456",settingsViewModel.getOldPassword());
        assertNull(settingsViewModel.oldPasswordErrorTxt.getValue());
        settingsViewModel.setOldPassword("1234");
        assertEquals("1234",settingsViewModel.getOldPassword());
        assertNotNull(settingsViewModel.oldPasswordErrorTxt.getValue());

    }

    @Test
    public void setNewPassword() {
        assertNull(settingsViewModel.newPasswordErrorTxt.getValue());
        settingsViewModel.setNewPassword("123456");
        assertEquals("123456",settingsViewModel.getNewPassword());
        assertNull(settingsViewModel.newPasswordErrorTxt.getValue());
        settingsViewModel.setNewPassword("1234");
        assertEquals("1234",settingsViewModel.getNewPassword());
        assertNotNull(settingsViewModel.newPasswordErrorTxt.getValue());
    }

    @Test
    public void setReTypedPassword() {
        assertNull(settingsViewModel.reTypedErrorTxt.getValue());
        settingsViewModel.setNewPassword("12345678"); // This will set error to !=null because the reTyped field is empty
        assertNotNull(settingsViewModel.reTypedErrorTxt.getValue());
        ((MutableLiveData<String>)settingsViewModel.reTypedErrorTxt).postValue(null);  //Must reset null to test if setting identical PW will cause error
        settingsViewModel.setReTypedPassword("12345678");
        assertEquals("12345678",settingsViewModel.getReTypedPassword());
        assertNull(settingsViewModel.reTypedErrorTxt.getValue());
        settingsViewModel.setReTypedPassword("123456");
        assertEquals("123456",settingsViewModel.getReTypedPassword());
        assertNotNull(settingsViewModel.reTypedErrorTxt.getValue());
    }

    @Test
    public void setUsername() {
        assertNull(settingsViewModel.usernameErrorTxt.getValue());
        settingsViewModel.setUsername("15chars-1234567");
        assertEquals("15chars-1234567",settingsViewModel.getUsername());
        assertNull(settingsViewModel.usernameErrorTxt.getValue());

        settingsViewModel.setUsername("16chars-12345678");
        assertEquals("16chars-12345678",settingsViewModel.getUsername());
        assertNotNull(settingsViewModel.usernameErrorTxt.getValue());
    }

    @Test
    public void getOldPassword() {
        assertEquals("", settingsViewModel.getOldPassword());
    }

    @Test
    public void getNewPassword() {
        assertEquals("", settingsViewModel.getNewPassword());
    }

    @Test
    public void getReTypedPassword() {
        assertEquals("", settingsViewModel.getReTypedPassword());
    }

    @Test
    public void getUsername() {
        assertEquals("", settingsViewModel.getUsername());
    }



    @Test
    public void refreshUsr() {
        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void refreshUsr() {
               methodInvoked = true;
            }
        });
        assertFalse(methodInvoked);
        settingsViewModel.refreshUsr();
        assertTrue(methodInvoked);
    }

    @Test
    public void updateUserProfile() {
        String liveUserUsername = "usersCurrentName";

        final LiveData<User> userLiveDataTest = new MutableLiveData<>(new User(liveUserUsername, null, null, null, true));
        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback) {
                fail();
            }

            @Override
            public LiveData<User> getUserLiveData() {
                return userLiveDataTest;
            }
        });

        assertFalse(settingsViewModel.updateUserProfile());
        assertNotNull(settingsViewModel.usernameErrorTxt.getValue());
        settingsViewModel.setUsername("1234567890123456"); // to long
        assertFalse(settingsViewModel.updateUserProfile());
        settingsViewModel.setUsername(liveUserUsername); // Identical to User - username
        assertFalse(settingsViewModel.updateUserProfile());

        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void updateProfile(String userName, Uri pictureUri, OnCompleteListener callback) {
                assertEquals(testUsername, userName);
                assertEquals(testURi, pictureUri);
                ((MutableLiveData)userLiveDataTest).postValue(new User(userName, null, pictureUri, null, true));
                assertTrue(settingsViewModel.savingNewProfile.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(settingsViewModel.savingNewProfile.getValue());
                methodInvoked = true;
            }
            @Override
            public LiveData<User> getUserLiveData() {
                return userLiveDataTest;
            }
        });
        testUsername = "usersNewName";
        testURi = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
       settingsViewModel.setNewProfilePicture(testURi); settingsViewModel.setUsername(testUsername);
        assertFalse(methodInvoked);
        assertTrue(settingsViewModel.updateUserProfile()); //Checking if saves with both values set.
        assertTrue(methodInvoked); methodInvoked = false;

        assertFalse(settingsViewModel.updateUserProfile()); //Checking if it saves with same settings
        assertFalse(methodInvoked);

        testURi = null; testUsername = "otherNewUserN";
        settingsViewModel.setUsername(testUsername);
        assertTrue(settingsViewModel.updateUserProfile()); // Checking it saves only username
        assertTrue(methodInvoked); methodInvoked = false;

        assertNull(settingsViewModel.getNewProfilePicture());
        testURi =Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
        settingsViewModel.setNewProfilePicture(testURi);
        assertTrue(settingsViewModel.updateUserProfile()); // Checking it saves only profile picture, parameter name should be identical
        assertTrue(methodInvoked);



    }

    @Test
    public void updateUserPassword() {
        String failPW = "12345";
        String passPW = "123456";

        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
                fail();
            }
        });

        attemptPasswordChange("", "", "", settingsViewModel, false);
        assertNotNull(settingsViewModel.oldPasswordErrorTxt.getValue());
        assertNotNull(settingsViewModel.newPasswordErrorTxt.getValue());
        assertNotNull(settingsViewModel.reTypedErrorTxt.getValue());
        attemptPasswordChange(failPW, failPW, failPW, settingsViewModel, false);
        attemptPasswordChange(failPW, failPW, passPW, settingsViewModel, false);
        attemptPasswordChange(failPW, passPW, failPW, settingsViewModel, false);
        attemptPasswordChange(failPW, passPW, passPW, settingsViewModel, false);
        attemptPasswordChange(passPW, failPW, failPW, settingsViewModel, false);
        attemptPasswordChange(passPW, failPW, passPW, settingsViewModel, false);
        attemptPasswordChange(passPW, passPW, failPW, settingsViewModel, false);

        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
                assertEquals(passPW, oldPassword);
                assertEquals(passPW, newPassword);
                assertTrue(settingsViewModel.changingPassword.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(settingsViewModel.changingPassword.getValue());
                methodInvoked = true;
            }
        });

        assertFalse(methodInvoked);
        attemptPasswordChange(passPW, passPW, passPW, settingsViewModel, true);
        assertTrue(methodInvoked);
    }

    @Test
    public void resendVerificationEmail() {
        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void reSendVerificationEmail(OnCompleteListener callback) {
                assertTrue(settingsViewModel.sendingVerificationEmail.getValue());
                callback.onComplete(new MockTaskResponse().setSuccessful(true));
                assertFalse(settingsViewModel.sendingVerificationEmail.getValue());
                methodInvoked = true;

            }
        });
        assertFalse(methodInvoked);
        settingsViewModel.resendVerificationEmail();
        assertTrue(methodInvoked);
    }

    private void attemptPasswordChange(String oldPw, String pw, String reTypedpw, AccountSettingsViewModel accountSettingsViewModel, boolean exp){
        accountSettingsViewModel.setOldPassword(oldPw);
        accountSettingsViewModel.setNewPassword(pw);
        accountSettingsViewModel.setReTypedPassword(reTypedpw);
       assertEquals(exp,accountSettingsViewModel.updateUserPassword());
    }
}