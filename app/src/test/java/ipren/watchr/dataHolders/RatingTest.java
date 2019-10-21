package ipren.watchr.dataHolders;

import org.junit.Test;

import static org.junit.Assert.*;

public class RatingTest {

    @Test
    public void getMovie_id() {
        Rating testRating = new Rating();
        assertEquals("", testRating.getMovie_id());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getMovie_id());
        String testMovieID = "TESTVALUE";
        testRating = new Rating(testMovieID, null, null  ,0);
        assertEquals(testMovieID,testRating.getMovie_id());
    }

    @Test
    public void getUser_id() {
        Rating testRating = new Rating();
        assertEquals("", testRating.getUser_id());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getUser_id());
        String testUsedID = "TESTVALUE";
        testRating = new Rating(null, testUsedID, null  ,0);
        assertEquals(testUsedID,testRating.getUser_id());
    }

    @Test
    public void getuID() {
        Rating testRating = new Rating();
        assertEquals("", testRating.getuID());
        testRating = new Rating(null, null,null,0);
        assertEquals("", testRating.getuID());
        String testRatingUID = "TESTVALUE";
        testRating = new Rating(null, null, testRatingUID  ,0);
        assertEquals(testRatingUID,testRating.getuID());
    }

    @Test
    public void getScore() {
        Rating testRating = new Rating();
        assertEquals(0, testRating.getScore());
        testRating = new Rating(null, null,null,0);
        assertEquals(0, testRating.getScore());
    }
}