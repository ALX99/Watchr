package ipren.watchr.Activities;


import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.tools.ant.Main;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import ipren.watchr.DataHolders.User;
import ipren.watchr.R;
import ipren.watchr.ViewModels.MainViewModel;
import ipren.watchr.ViewModels.MainViewModelInterface;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    //This variable passes the mockViewModel to the TestActivity. REASON: Robolectric does not accept anonymous classes
    static MockViewModel mockViewModel;

    //Creates a fresh MockViewModel for each method.
    @Before
    public void setUp() {
        // This can be overriden in a method if its replaced before calling buildActivity.
        // Will be reset on nextTest
        mockViewModel = new MockViewModel();
    }

    @Test
    public void onLoginButtonTest() {
        ActivityController mainActivtyController = Robolectric.buildActivity(MainActivityTestclass.class);
        mainActivtyController.create().start().visible();
        MainActivity activity = ((MainActivity) mainActivtyController.get());
        //Assuming the standard MockViewModel was used <----- Warning if the standard MockViewModel is changed so must this. This is what it should show when user == null
        Assert.assertTrue(((ActionMenuItemView) activity.findViewById(R.id.login_button)).getText().equals("Login"));
        String[] userValues = {"David", "Frank", "ÖÄÅØÆæø", "!#¤%&/()=?`-/3$", ""}; // I could make this more extensive but...
        for (String value : userValues) {
            mockViewModel.setUser(new User(value));
            Assert.assertTrue(((ActionMenuItemView) activity.findViewById(R.id.login_button)).getText().equals(value));
        }
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