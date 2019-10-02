package ipren.watchr.viewModels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.dataHolders.User;

import static org.junit.Assert.*;

//This class is based on preset dummy values //TODO Expand this class once the repository is created and inject a mock repository instead

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainViewModelTest {
    MainViewModel mainViewModel;
    @Before
    public void setUp(){
        mainViewModel = new MainViewModel();
    }
    @Test
    public void getUser() {
        assertNull(mainViewModel.getUser().getValue());
        mainViewModel.loginUser("david@ipren.com","123456");
        User user = mainViewModel.getUser().getValue();
        assertTrue(user.getEmail().equalsIgnoreCase("david@ipren.com"));
        assertTrue(user.getUserName().equalsIgnoreCase("David Olsson"));
        assertNull(user.getUserProfilePicture());
    }

    @Test
    public void isEmailRegistered() {
        assertTrue(mainViewModel.isEmailRegistered("david@ipren.com"));
        assertFalse(mainViewModel.isEmailRegistered("fake@ipren.com"));
    }

    @Test
    public void loginUser() {
        assertNull(mainViewModel.getUser().getValue());
        assertTrue(mainViewModel.loginUser("david@ipren.com","123456"));
        assertEquals(mainViewModel.getUser().getValue().getEmail(), "david@ipren.com");
        assertEquals(mainViewModel.getUser().getValue().getUserName(), "David Olsson");
    }

    @Test
    public void logoutCurrentUser() {
        assertNull(mainViewModel.getUser().getValue());
        assertTrue(mainViewModel.loginUser("david@ipren.com","123456"));
        assertNotNull(mainViewModel.getUser().getValue());
        mainViewModel.logoutCurrentUser();
        assertNull(mainViewModel.getUser().getValue());
    }
}