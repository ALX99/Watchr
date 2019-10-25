package ipren.watchr.dataholders;

import android.net.Uri;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class PublicProfileTest {


    @Test
    public void getUsername() {
        String testName = "testName";
        PublicProfile testProfile = new PublicProfile(null, testName);
        assertEquals(testName, testProfile.getUsername());

        testProfile = new PublicProfile(null, null);
        assertEquals("Not set", testProfile.getUsername());

    }

    @Test
    public void getProfilePhotoUri() {
        Uri testUri = Uri.parse("www.test.com/testfile.xml");
        PublicProfile testProfile = new PublicProfile(testUri, null);
        assertEquals(testUri, testProfile.getProfilePhotoUri());

        Uri expectedURi = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
        testProfile = new PublicProfile(null, null);
        assertEquals(testProfile.getProfilePhotoUri(), expectedURi);
    }


}