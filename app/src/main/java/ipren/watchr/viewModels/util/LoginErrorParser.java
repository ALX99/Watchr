package ipren.watchr.viewModels.util;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginErrorParser {

    //Parses Authentication errors from fireBase
    public static String parseAuthError(Exception exception) {
        if (exception == null) {
            return "";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return ((FirebaseAuthInvalidCredentialsException) exception).getErrorCode().replace("ERROR_", "");
        } else if (exception instanceof FirebaseAuthInvalidUserException)
            return ((FirebaseAuthInvalidUserException) exception).getErrorCode().replace("ERROR_", "");
        else
            return exception.getLocalizedMessage();

    }
}
