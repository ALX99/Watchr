package ipren.watchr.dataholders;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class RatingTest {
    Rating testRating;
    @Before
    public void setUp(){
        testRating = new Rating();
    }

    @Test
    public void getMovie_id() {
        assertEquals("", testRating.getMovie_id());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getMovie_id());
        String testMovieID = "TESTVALUE";
        testRating = new Rating(testMovieID, null, null  ,0);
        assertEquals(testMovieID,testRating.getMovie_id());
    }

    @Test
    public void getUser_id() {
        assertEquals("", testRating.getUser_id());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getUser_id());
        String testUsedID = "TESTVALUE";
        testRating = new Rating(null, testUsedID, null  ,0);
        assertEquals(testUsedID,testRating.getUser_id());
    }

    @Test
    public void getuID() {
        assertEquals("", testRating.getuID());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getuID());
        String testRatingUID = "TESTVALUE";
        testRating = new Rating(null, null, testRatingUID  ,0);
        assertEquals(testRatingUID,testRating.getuID());
    }

    @Test
    public void getScore() {
        assertEquals(0, testRating.getScore());
        testRating = new Rating(null, null,null,0);
        assertEquals(0, testRating.getScore());
    }



}
