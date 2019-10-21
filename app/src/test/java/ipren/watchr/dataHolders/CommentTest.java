package ipren.watchr.dataHolders;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void getMovie_id() {
        String testString = "testValue";
        Comment testComment = new Comment(testString, "Not used", "Not used", "Not Used");
        assertEquals(testComment.getMovie_id(), testString);

    }

    @Test
    public void getUser_id() {
        String testString = "testValue";
        Comment testComment = new Comment("not used", testString, "Not used", "Not Used");
        assertEquals(testComment.getUser_id(), testString);

    }

    @Test
    public void getText() {
        String testString = "testValue";
        Comment testComment = new Comment("Not used", "Not used", testString, "Not Used");
        assertEquals(testComment.getText(), testString);

    }

    @Test
    public void getuID() {
        String testString = "testValue";
        Comment testComment = new Comment("Not used", "Not used", "Not used", testString);
        assertEquals(testComment.getuID(), testString);

    }

}