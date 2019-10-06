package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import ipren.watchr.MockClasses.MockMainRepository;
import ipren.watchr.dataHolders.User;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class AccountViewModelTest {

    private AccountViewModel accountViewModel;
    private final User inititalUser = new User("David", "david@ipren.com");
    @Before
    public void setUp(){
        accountViewModel = new AccountViewModel(new MockMainRepository(inititalUser));
    }

    @Test
    public void getUser() {
        MockMainRepository mockRepo = new MockMainRepository();
        accountViewModel = new AccountViewModel(mockRepo);

        User testUser = new User("Test", "test@ipren.com1");
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