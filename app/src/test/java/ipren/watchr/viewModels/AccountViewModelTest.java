package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import ipren.watchr.MockClasses.MockUserDataRepository;
import ipren.watchr.dataHolders.User;

//TODO remake this test
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class AccountViewModelTest {

    private AccountViewModel accountViewModel;
    private final User inititalUser = new User("Fred", "Fred@test.com", null, null, false);
    @Before
    public void setUp(){
        accountViewModel = new AccountViewModel(new MockUserDataRepository(inititalUser));
    }

    @Test
    public void getUser() {
        MockUserDataRepository mockRepo = new MockUserDataRepository();
        accountViewModel = new AccountViewModel(mockRepo);

        User testUser = new User("Test", "test@test.com1", null, null, false);
        LiveData<User> liveUser = accountViewModel.getUser();
        assertNotEquals(liveUser.getValue(), testUser);
        assertNotEquals(accountViewModel.getUser().getValue(), liveUser);
        mockRepo.setUser(testUser);
        assertEquals(liveUser.getValue(), testUser);
        assertEquals(accountViewModel.getUser().getValue(), testUser);

    }

    @Test
    public void signOut() {
       LiveData<User> liveUser = accountViewModel.getUser();
       assertNotNull(liveUser.getValue());
       accountViewModel.signOut();
       assertNull(liveUser.getValue());
    }




}