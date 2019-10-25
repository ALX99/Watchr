package ipren.watchr.viewmodels.util;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.isEmailFormat;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.postValue;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.setStringTooLongErrorTxt;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updateEmailErrorTxt;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updatePasswordErrorTxt;
import static ipren.watchr.viewmodels.util.ViewModelSupportUtils.updatePasswordMatchErrorTxt;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class ViewModelSupportUtilsTest {
    LiveData<String> errorTxt;

    @Before
    public void setUp() {
        errorTxt = new MutableLiveData<>();
    }

    @Test
    public void postValueTest() {
        LiveData<Boolean> testLivadata = new MutableLiveData<>(null);
        assertNull(testLivadata.getValue());
        postValue(testLivadata, true);
        assertNotNull(testLivadata.getValue());
        assertTrue(testLivadata.getValue());
        postValue(testLivadata, null);
        assertNull(testLivadata.getValue());
    }

    @Test
    public void updateEmailErrorTxtTest() {
        assertNull(errorTxt.getValue());
        updateEmailErrorTxt(errorTxt, "valid@test.com");
        assertNull(errorTxt.getValue());
        updateEmailErrorTxt(errorTxt, "");
        assertNull(errorTxt.getValue());
        updateEmailErrorTxt(errorTxt, "invalidemail");
        assertNotNull(errorTxt.getValue());

    }

    @Test
    public void updatePasswordErrorTxtTest() {
        int minChars = 5;
        assertNull(errorTxt.getValue());
        updatePasswordErrorTxt(errorTxt, "12345", minChars);
        assertNull(errorTxt.getValue());
        updatePasswordErrorTxt(errorTxt, "1234", minChars);
        assertNotNull(errorTxt.getValue());
    }

    @Test
    public void updatePasswordMatchErrorTxtTest() {
        assertNull(errorTxt.getValue());
        updatePasswordMatchErrorTxt(errorTxt, "match", "match");
        assertNull(errorTxt.getValue());
        updatePasswordMatchErrorTxt(errorTxt, "dont", "match");
        assertNotNull(errorTxt.getValue());
        postValue(errorTxt, null);
        updatePasswordMatchErrorTxt(errorTxt, "MaTcH", "match");
        assertNotNull(errorTxt.getValue());
    }

    @Test
    public void isEmailFormatTest() {
        assertTrue(isEmailFormat("valid@sl.com"));
        assertTrue(isEmailFormat("valid@gmail.com"));
        assertFalse(isEmailFormat(null));
        assertFalse(isEmailFormat("notvalid.com"));
        assertFalse(isEmailFormat("notvalid@thiscom"));
    }

    @Test
    public void setStringTooLongErroTxtTest() {
        int maxChars = 6;
        assertNull(errorTxt.getValue());
        setStringTooLongErrorTxt(errorTxt, "123456", maxChars);
        assertNull(errorTxt.getValue());
        setStringTooLongErrorTxt(errorTxt, "1234567", maxChars);
        assertNotNull(errorTxt.getValue());
    }
}