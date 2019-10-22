package ipren.watchr.dataHolders;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class CommentTest {

    Date testDate;
    String testString;
    Comment nullConstructor;
    @Before
    public void setUp(){
        testDate = Calendar.getInstance().getTime();
        testString = "testValue";
        nullConstructor = new Comment(null,null,null,null,null);
    }
    @Test
    public void getMovie_id() {
        assertNull(nullConstructor.getMovie_id());
        Comment testComment = new Comment(testString, "Not used", "Not used", "Not Used", testDate);
        assertEquals(testComment.getMovie_id(), testString);
        testComment = new Comment(testString, null, null, null, null);
        assertEquals(testComment.getMovie_id(), testString);

    }

    @Test
    public void getUser_id() {
        assertNull(nullConstructor.getUser_id());
        Comment testComment = new Comment("not used", testString, "Not used", "Not Used", testDate);
        assertEquals(testComment.getUser_id(), testString);
        testComment = new Comment(null, testString,null , null, null);
        assertEquals(testComment.getUser_id(), testString);

    }

    @Test
    public void getText() {
        assertNull(nullConstructor.getText());
        Comment testComment = new Comment("Not used", "Not used", testString, "Not Used", testDate);
        assertEquals(testComment.getText(), testString);
        testComment = new Comment(null, null, testString, null, null);
        assertEquals(testComment.getText(), testString);

    }

    @Test
    public void getuID() {
        assertNull(nullConstructor.getuID());
        Comment testComment = new Comment("Not used", "Not used", "Not used", testString, testDate);
        assertEquals(testComment.getuID(), testString);
        testComment = new Comment(null, null , null, testString, null);
        assertEquals(testComment.getuID(), testString);

    }

    @Test
    public void getDate(){
        assertNull(nullConstructor.getDate_created());
        Comment testComment = new Comment("Not used", "Not used", "Not used", "Not used", testDate);
        assertEquals(testComment.getDate_created(), testDate);
        testComment = new Comment(null, null , null, null, testDate);
        assertEquals(testComment.getDate_created(), testDate);
    }









}