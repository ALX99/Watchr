package ipren.watchr.viewModels.util;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelSupportUtils {

    public static <T> void postValue(LiveData<T> liveData, T object) {
        try {
            ((MutableLiveData<T>) liveData).postValue(object);
        } catch (Exception e) {
            Log.e("Watchr", "unable to post value");
        }
    }

    public static void updateEmailErrorTxt(LiveData<String> live, String text) {
        if (!isEmailFormat(text))
            postValue(live, "Not an email address");
    }

    public static void updatePasswordErrorTxt(LiveData<String> live, String text) {
        if (!text.isEmpty() && text.length() < 6)
            postValue(live, "Must be more than five characters");
    }

    public static void updatePasswordMatchErrorTxt(LiveData<String> live, String pw, String rePw) {
        if (!pw.equals(rePw))
            postValue(live, "Passwords don't match");
    }

    public static boolean isEmailFormat(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void setStringTooLongErroTxt(LiveData<String> live, String username, int maxChars){
        if(username.length() > maxChars)
            postValue(live, "Max 15 characters");
    }

}
