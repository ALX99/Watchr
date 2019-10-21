package ipren.watchr.dataHolders;

import android.net.Uri;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class UserTest {
    private String expUserName = "usernameTestDef";
    private String expEmail = "emailTestDef";
    private boolean expIsVerified = true;
    private String expUID = "UIDtestDef";
    private Uri expProfilePicUri = Uri.parse("www.google.com/picture");
    private User testUser = new User(expUserName, expEmail, expProfilePicUri, expUID, expIsVerified);
    private User nullConstructUSer = new User(null, null, null, null, false);

    @Before
    public void setUp() {
        testUser = new User(expUserName, expEmail, expProfilePicUri, expUID, expIsVerified);
        nullConstructUSer = new User(null, null, null, null, false);

    }

    @Test
    public void getUID() {
        assertEquals("UID missing", nullConstructUSer.getUID());
        assertEquals(expUID, testUser.getUID());

    }

    @Test
    public void isVerified() {
        assertFalse(nullConstructUSer.isVerified());
        assertEquals(expIsVerified, testUser.isVerified());

    }

    @Test
    public void getUserName() {
        assertEquals("", nullConstructUSer.getUserName());
        assertEquals(expUserName, testUser.getUserName());

    }

    @Test
    public void getEmail() {
        assertEquals("", nullConstructUSer.getEmail());
        assertEquals(expEmail, testUser.getEmail());

    }

    @Test
    public void getUserProfilePictureUri() {
        Uri userProfilePictureUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
        assertEquals(userProfilePictureUri, nullConstructUSer.getUserProfilePictureUri());
        assertEquals(expProfilePicUri, testUser.getUserProfilePictureUri());

    }
}