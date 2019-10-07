package ipren.watchr.viewModels.util;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginErrorParser {
    //TODO Rework error parsing
    public static String parseAuthError(Exception exception) {
        if(exception == null) {
            return "";
        }else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return ((FirebaseAuthInvalidCredentialsException) exception).getErrorCode();
        } else if (exception instanceof FirebaseAuthInvalidUserException)
            return ((FirebaseAuthInvalidUserException) exception).getErrorCode();
        else
            return exception.getLocalizedMessage();

    }
}
