package ipren.watchr.viewModels;

<<<<<<< HEAD
import androidx.lifecycle.LiveData;
=======
import android.os.Build;
>>>>>>> master

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.MockClasses.MockMainRepository;
import ipren.watchr.dataHolders.User;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MainViewModelTest {
    private MainViewModel mainViewModel;
    private MockMainRepository mockMainRepository;
    private final User initialUser = new User("David", "david@ipren.com");
    @Before
    public void setUp(){
        mockMainRepository = new MockMainRepository(initialUser);
        mainViewModel = new MainViewModel(mockMainRepository);
    }

    @Test
    public void getUser() {
        User testUser = new User("Test", "test@ipren.com1");
        LiveData<User> liveUser = mainViewModel.getUser();
        assertNotEquals(liveUser.getValue(), testUser);
        assertNotEquals(mainViewModel.getUser().getValue(), liveUser);
        mockMainRepository.setUser(testUser);
        assertEquals(liveUser.getValue(), testUser);
        assertEquals(mainViewModel.getUser().getValue(), testUser);
    }
}