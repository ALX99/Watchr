package ipren.watchr.viewModels;

import android.net.Uri;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.BuildConfig;
import ipren.watchr.MockClasses.UserDataRepositoryAdapter;
import ipren.watchr.R;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class AccountSettingsViewModelTest {
    AccountSettingsViewModel settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter());
    @Before
    public void setUp(){
        settingsViewModel = new AccountSettingsViewModel(new UserDataRepositoryAdapter());
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
        settingsViewModel.setNewPassword("12345678");
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
    }

    @Test
    public void updateUserProfile() {
    }

    @Test
    public void updateUserPassword() {
    }
    @Test
    public void resendVerificationEmail() {
    }

    @Test
    public void otherVariables(){

    }


}