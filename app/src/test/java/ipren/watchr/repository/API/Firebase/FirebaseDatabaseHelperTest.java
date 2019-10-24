package ipren.watchr.repository.API.Firebase;

import android.os.Build;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.core.FirestoreClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class FirebaseDatabaseHelperTest {

    @Test
    public void syncUserWithDatabase() {
    }

    @Test
    public void saveMovieToList() {
    }

    @Test
    public void deleteMovieFromList() {
    }

    @Test
    public void getMovieListByUserID() {
    }

    @Test
    public void addComment() {
    }

    @Test
    public void removeComment() {
    }

    @Test
    public void addRating() {
    }

    @Test
    public void removeRating() {
    }

    @Test
    public void getCommentByMovieID() {
    }

    @Test
    public void getCommentsByUserID() {
    }

    @Test
    public void getRatingByUserID() {
    }

    @Test
    public void getRatingByMovieID() {
    }

    @Test
    public void getPublicProfile() {
    }
}