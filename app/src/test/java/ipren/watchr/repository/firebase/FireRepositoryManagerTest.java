package ipren.watchr.repository.firebase;

import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.BuildConfig;
import ipren.watchr.MockClasses.OnCompleteListenerAdapter;
import ipren.watchr.R;
import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.PublicProfile;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.repository.IUserDataRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class FireRepositoryManagerTest {
    private final String TEST_ID = "98765";
    private final String TEST_ID2 = "lkopalskdf";
    private final String TEST_TEXT = "TEST_TEXT";

    private final String TEST_LIST = "FAVORITES";
    private final String TEST_EMAIL = "testEmail";
    private final String TEST_PW_1 = "123456";
    private final String TEST_PW_2 = "654321";
    private final String TEST_USERNAME = "TESTUSER";
    private final Uri TEST_URI = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
    private final OnCompleteListener referenceTest = new OnCompleteListenerAdapter();

    private int counter = 0;


    @Before
    public void setUp() {
        counter = 0;
    }

    //FireAuth
    @Test(expected = MethodInvokedCallBack.class)
    public void resetPassword() {
        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void resetPassword(String email, OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                assertEquals(TEST_EMAIL, email);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.resetPassword(TEST_EMAIL, referenceTest);
    }

    @Test
    public void getUserLiveData() {
    }


    @Test(expected = MethodInvokedCallBack.class)
    public void registerUser() {
        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void registerUser(String email, String password, OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                assertEquals(TEST_EMAIL, email);
                assertEquals(TEST_PW_1, password);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.registerUser(TEST_EMAIL, TEST_PW_1, referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void signOutUser() {

        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void signOut() {
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.signOutUser();

    }


    @Test(expected = MethodInvokedCallBack.class)
    public void loginUser() {

        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void loginUser(String email, String password, OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                assertEquals(TEST_EMAIL, email);
                assertEquals(TEST_PW_1, password);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.loginUser(TEST_EMAIL, TEST_PW_1, referenceTest);

    }

    @Test(expected = MethodInvokedCallBack.class)
    public void refreshUsr() {

        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void refreshUsr() {
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.refreshUsr();
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void reSendVerificationEmail() {

        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void resendVerificationEmail(OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.reSendVerificationEmail(referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void updateProfile() {

        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void updateProfile(String userName, Uri uri, OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                assertEquals(TEST_USERNAME, userName);
                assertEquals(TEST_URI, uri);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.updateProfile(TEST_USERNAME, TEST_URI, referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void changePassword() {
        FireRepositoryManager repo = new FireRepositoryManager(new FireBaseAuthHelper(null) {
            @Override
            void changePassword(String oldPassword, String newPassword, OnCompleteListener callback) {
                assertTrue(referenceTest == callback);
                assertEquals(TEST_PW_1, oldPassword);
                assertEquals(TEST_PW_2, newPassword);
                throw new MethodInvokedCallBack();
            }
        }, null);
        repo.changePassword(TEST_PW_1, TEST_PW_2, referenceTest);
    }

    //Database

    @Test(expected = MethodInvokedCallBack.class)
    public void getPublicProfile() {

        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {

            @Override
            LiveData<PublicProfile> getPublicProfile(String user_id) {
                assertEquals(TEST_ID, user_id);
                throw new MethodInvokedCallBack();
            }
        });
        repo.getPublicProfile(TEST_ID);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void getCommentsByMovie_ID() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            LiveData<Comment[]> getCommentByMovieID(String movie_id) {
                assertEquals(TEST_ID, movie_id);
                throw new MethodInvokedCallBack();
            }
        });

        repo.getComments(TEST_ID, IUserDataRepository.SEARCH_METHOD_MOVIE_ID);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void getCommentsByUser_ID() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            LiveData<Comment[]> getCommentsByUserID(String user_id) {
                assertEquals(TEST_ID, user_id);
                throw new MethodInvokedCallBack();
            }
        });
        repo.getComments(TEST_ID, IUserDataRepository.SEARCH_METHOD_USER_ID);

    }

    @Test(expected = MethodInvokedCallBack.class)
    public void getRatingsByMovie_id() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            LiveData<Rating[]> getRatingByMovieID(String movie_id) {
                assertEquals(TEST_ID, movie_id);
                throw new MethodInvokedCallBack();
            }
        });
        repo.getRatings(TEST_ID, IUserDataRepository.SEARCH_METHOD_MOVIE_ID);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void getRatingsByUser_id() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            LiveData<Rating[]> getRatingByUserID(String user_id) {
                assertEquals(TEST_ID, user_id);
                throw new MethodInvokedCallBack();
            }
        });
        repo.getRatings(TEST_ID, IUserDataRepository.SEARCH_METHOD_USER_ID);
    }


    @Test(expected = MethodInvokedCallBack.class)
    public void addMovieToList() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            public void saveMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {
                assertEquals(TEST_LIST, list);
                assertEquals(TEST_ID, movie_id);
                assertEquals(TEST_ID2, user_id);
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        });

        repo.addMovieToList(TEST_LIST, TEST_ID, TEST_ID2, referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void getMovieList() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            public LiveData<String[]> getMovieListByUserID(String list, String user_id) {
                assertEquals(TEST_LIST, list);
                assertEquals(TEST_ID, user_id);
                throw new MethodInvokedCallBack();
            }
        });
        repo.getMovieList(TEST_LIST, TEST_ID);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void removeMovieFromList() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            public void deleteMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {
                assertEquals(TEST_LIST, list);
                assertEquals(TEST_ID, movie_id);
                assertEquals(TEST_ID2, user_id);
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        });

        repo.removeMovieFromList(TEST_LIST, TEST_ID, TEST_ID2, referenceTest);
    }

    @Test
    public void rateMovie() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            void addRating(int score, String movie_id, String user_id, OnCompleteListener callback) {
                assertTrue((score <= 10 && score >= 0));
                assertEquals(TEST_ID, movie_id);
                assertEquals(TEST_ID2, user_id);
                assertTrue(referenceTest == callback);
                counter++;
            }
        });

        repo.rateMovie(-1, TEST_ID, TEST_ID2, referenceTest);
        repo.rateMovie(11, TEST_ID, TEST_ID2, referenceTest);
        repo.rateMovie(10, TEST_ID, TEST_ID2, referenceTest);
        repo.rateMovie(0, TEST_ID, TEST_ID2, referenceTest);
        repo.rateMovie(5, TEST_ID, TEST_ID2, referenceTest);
        assertTrue(counter == 5);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void removeRating() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            void removeRating(String rating_id, OnCompleteListener callback) {
                assertEquals(TEST_ID, rating_id);
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        });
        repo.removeRating(TEST_ID, referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void commentMovie() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            void addComment(String text, String movie_id, String user_id, OnCompleteListener callback) {
                assertEquals(TEST_TEXT, text);
                assertEquals(TEST_ID, movie_id);
                assertEquals(TEST_ID2, user_id);
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        });
        repo.commentMovie(TEST_TEXT, TEST_ID, TEST_ID2, referenceTest);
    }

    @Test(expected = MethodInvokedCallBack.class)
    public void removeComment() {
        FireRepositoryManager repo = new FireRepositoryManager(null, new FireBaseDatabaseHelper(null) {
            @Override
            void removeComment(String comment_id, OnCompleteListener callback) {
                assertEquals(TEST_ID, comment_id);
                assertTrue(referenceTest == callback);
                throw new MethodInvokedCallBack();
            }
        });
        repo.removeComment(TEST_ID, referenceTest);
    }

    private class MethodInvokedCallBack extends Error {
    }
}