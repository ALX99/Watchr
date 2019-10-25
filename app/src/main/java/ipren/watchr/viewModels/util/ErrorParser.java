package ipren.watchr.viewModels.util;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginErrorParser {

    //Parses Authentication errors from fireBase
    public static String parseAuthError(Exception exception) {
        if (exception == null) {
            return "";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return ((FirebaseAuthInvalidCredentialsException) exception).getErrorCode().replace("ERROR_", "").replace("_", " ");
        } else if (exception instanceof FirebaseAuthInvalidUserException){
            return ((FirebaseAuthInvalidUserException) exception).getErrorCode().replace("ERROR_", "").replace("_", " ");
        }else if(exception instanceof FirebaseTooManyRequestsException){
            return "Too many failed attempts, try again later";
        } else {
            String errorTxt = exception.getLocalizedMessage();
            return errorTxt.equals("An internal error has occurred. [ 7: ]") ? "Network error, try again later" : errorTxt;
        }


    }
}
