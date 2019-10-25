package ipren.watchr.viewmodels.util;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//A helper class for easier LiveData management and String parsing.
public class ViewModelSupportUtils {
    //Helper method for updating LiveData, attempts to cast to MutableLiveData where postValue is available
    public static <T> void postValue(LiveData<T> liveData, T object) {
        try {
            ((MutableLiveData<T>) liveData).postValue(object);
        } catch (Exception e) {
            Log.e("Watchr", "unable to post value");
        }
    }

    //Checks if a String follows  email format, if not posts error to LiveData
    public static void updateEmailErrorTxt(LiveData<String> live, String text) {
        if (!isEmailFormat(text) && !text.isEmpty())
            postValue(live, "Not an email address");
        else
            postValue(live, null);
    }

    //Checks if a password string is long enough, if it's not post error to LiveData
    public static void updatePasswordErrorTxt(LiveData<String> live, String text, int minChars) {
        if (!text.isEmpty() && text.length() < minChars)
            postValue(live, "Password is too short");
        else
            postValue(live, null);
    }

    //Checks if two password are identical, if they are not it post error to LiveData
    public static void updatePasswordMatchErrorTxt(LiveData<String> live, String pw, String rePw) {
        if (!pw.equals(rePw))
            postValue(live, "Passwords don't match");
        else
            postValue(live, null);
    }

    //Helper method for checking if a string follows email format
    public static boolean isEmailFormat(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Checks if a string is over a certain lenght, if it is posts error to LiveData
    public static void setStringTooLongErrorTxt(LiveData<String> live, String username, int maxChars) {
        if (username.length() > maxChars)
            postValue(live, "Max 15 characters");
        else
            postValue(live, null);
    }

}
