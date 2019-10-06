package ipren.watchr.dataHolders;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthenticationResponseTest {

    @Test
    public void isSucessfull() {
        AuthenticationResponse autResp = new AuthenticationResponse(true);
        assertTrue(autResp.isSuccessful());
        autResp = new AuthenticationResponse(false);
        assertFalse(autResp.isSuccessful());
        autResp = new AuthenticationResponse(true, "Not used");
        assertTrue(autResp.isSuccessful());
        autResp = new AuthenticationResponse(false , "Not used");
        assertFalse(autResp.isSuccessful());
    }

    @Test
    public void getErrorMsg() {
        String testValue = "Test Value";
        AuthenticationResponse autResp = new AuthenticationResponse(true);
        assertEquals("", autResp.getErrorMsg());
        autResp = new AuthenticationResponse(false);
        assertEquals("", autResp.getErrorMsg());
        autResp = new AuthenticationResponse(true, testValue);
        assertEquals(testValue, autResp.getErrorMsg());
        autResp = new AuthenticationResponse(false , testValue);
        assertEquals(testValue, autResp.getErrorMsg());
    }
}