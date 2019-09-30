package ipren.watchr.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;


import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import ipren.watchr.model.User;
import ipren.watchr.R;
import ipren.watchr.viewmodel.MainViewModelInterface;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {

    //Helper method to convert vector images to bitmaps for comparison
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
    //Convenience method to avoid some code duplication
    private static ActivityController getVisibleActivity(Class activityClass) {
        ActivityController mainActivityController = Robolectric.buildActivity(activityClass);
        mainActivityController.create().start().visible();
        return mainActivityController;
    }

    //This variable passes the mockViewModel to the TestActivity. REASON: Robolectric does not accept anonymous classes
    static MockViewModel mockViewModel;

    private Context appContext;

    //Creates a fresh MockViewModel for each method.
    @Before
    public void setUp() {
        // Will be reset on next test
        // This variable can be overriden in a test if its replaced before calling buildActivity.
        mockViewModel = new MockViewModel();
        appContext = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void onLoginButtonTest() {
        ActivityController mainActivtyController = getVisibleActivity(MainActivityTestclass.class);
        MainActivity activity = ((MainActivity) mainActivtyController.get());
        ActionMenuItemView testItem = activity.findViewById(R.id.login_button);
        //Assuming the standard MockViewModel was used <----- Warning if the standard MockViewModel is changed so must this. This is what it should show when user == null
        Assert.assertTrue(testItem.getText().equals("Login"));
        //Testing set values
        Assert.assertTrue(testItem.showsIcon()); //Making sure it is visible, TODO make a negative test.
        String[] userValues = {"David", "Frank", "ÖÄÅØÆæø", "!#¤%&/()=?`-/3$", ""}; // I could make this more extensive but...
        for (String value : userValues) {
            mockViewModel.setUser(new User(value));
            Assert.assertTrue(!testItem.getText().equals("Invalid value"));
            Assert.assertTrue(testItem.getText().equals(value));
        }
    }

    // This test assumes the preset resource type, vector vs bitmap , hence, different test methods for different images
    @Test
    public void profilePictureTest() {
        ActivityController mainActivtyController = getVisibleActivity(MainActivityTestclass.class);
        MainActivity activity = ((MainActivity) mainActivtyController.get());
        ActionMenuItemView testItem = activity.findViewById(R.id.profile_photo);

        //Assuming the standard MockViewModel was used <----- Warning if the standard MockViewModel is changed so must this. This is what it should show when user == null
        Assert.assertTrue(testItem.showsIcon());  //Making sure it is visible, TODO make a negative test.
        //Vector drawables needs special conversion when comparing, if they are not vector drawables test will crash
        Bitmap expectedBitmap = getBitmap((VectorDrawable) appContext.getResources().getDrawable(R.drawable.ic_no_user_photo_24px));
        Bitmap testBitmap = getBitmap((VectorDrawable) testItem.getItemData().getIcon());
        Assert.assertTrue(expectedBitmap.sameAs(testBitmap));
        //Negative test - with bitmap drawable
        expectedBitmap = ((BitmapDrawable) appContext.getResources().getDrawable(R.drawable.profile_picture_mock)).getBitmap();
        Assert.assertFalse(expectedBitmap.sameAs(testBitmap));
        //Switching profile image and testing, bitmap image, needs no
        mockViewModel.setUser(new User("Not Used", BitmapFactory.decodeResource(appContext.getResources(), R.drawable.profile_picture_mock)));
        testBitmap = ((BitmapDrawable) testItem.getItemData().getIcon()).getBitmap();
        Assert.assertTrue((expectedBitmap.sameAs(testBitmap)));
    }



}

//This class enables us to inject a custom viewmodel into MainActivity, cant be anonymous since..
//Robolectric cant instantiate it.
class MainActivityTestclass extends MainActivity {
    @Override
    protected MainViewModelInterface getViewModel() {
        return MainActivityTest.mockViewModel;
    }
}

// Custom ViewModel as a mock, in contrast to MainActivityTestClass this class can be anonymous as long as it implements MainViewModelInterface
//When changing this make sure it does not affect other methods in this class
class MockViewModel extends ViewModel implements MainViewModelInterface {

    private MutableLiveData<User> user = new MutableLiveData<>(null);

    @Override
    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.postValue(user);
    }


}