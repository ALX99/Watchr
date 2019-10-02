package ipren.watchr.dataHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.R;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class UserTest {
    Context appContext;

    //Needed to create bitmaps
    @Before
    public void setUp() {
        appContext = ApplicationProvider.getApplicationContext();
    }

    //Testing all constructors
    @Test
    public void getUserName() {
        User user = new User("expected - David", "not used");
        assertTrue(user.getUserName().equals("expected - David"));
        assertFalse(user.getUserName().equals("invalid"));

        user = new User();
        assertTrue(user.getUserName().equals("No user name"));

        Bitmap testBitMap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_no_user_photo_24px);
        user = new User("expected - Fred", "not used", testBitMap);
        assertTrue(user.getUserName().equals("expected - Fred"));
        assertFalse(user.getUserName().equals("invalid"));
    }

    //Testing all constructors
    @Test
    public void getUserProfilePicture() {
        User user = new User("Not used", "not used");
        assertNull(user.getUserProfilePicture());
        user = new User();
        assertNull(user.getUserProfilePicture());
        Bitmap testBitMap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_no_user_photo_24px);
        user = new User("Not used", "Not used", testBitMap);
        assertTrue(user.getUserProfilePicture().equals(testBitMap));
    }
    @Test
    public void getEmail(){
        User user = new User("Not used", "test@email.com");
        assertTrue(user.getEmail().equalsIgnoreCase("test@email.com"));

        user = new User();
        assertTrue(user.getEmail().equalsIgnoreCase("No email"));

        Bitmap testBitMap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_no_user_photo_24px);
        user = new User("Not used", "test@email.com", testBitMap);
        assertTrue(user.getEmail().equalsIgnoreCase("test@email.com"));


    }
}