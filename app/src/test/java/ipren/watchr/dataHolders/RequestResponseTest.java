package ipren.watchr.dataHolders;

import com.google.firebase.firestore.util.CustomClassMapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestResponseTest {

    @Test
    public void isSucessfull() {
        RequestResponse autResp = new RequestResponse(true);
        assertTrue(autResp.isSuccessful());
        autResp = new RequestResponse(false);
        assertFalse(autResp.isSuccessful());
        autResp = new RequestResponse(true, "Not used");
        assertTrue(autResp.isSuccessful());
        autResp = new RequestResponse(false , "Not used");
        assertFalse(autResp.isSuccessful());
    }

    @Test
    public void getErrorMsg() {
        String testValue = "Test Value";
        RequestResponse autResp = new RequestResponse(true);
        assertEquals("", autResp.getErrorMsg());
        autResp = new RequestResponse(false);
        assertEquals("", autResp.getErrorMsg());
        autResp = new RequestResponse(true, testValue);
        assertEquals(testValue, autResp.getErrorMsg());
        autResp = new RequestResponse(false , testValue);
        assertEquals(testValue, autResp.getErrorMsg());


    }
}