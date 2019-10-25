package ipren.watchr.viewmodels.util;

import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ErrorParser {

    //Parses Authentication errors from fireBase
    public static String parseError(Exception exception) {
        if (exception == null) {
            return "";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "That password is invalid";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return "There is no user connected to that email, it may have been deleted";
        } else if (exception instanceof FirebaseTooManyRequestsException) {
            return "Too many failed attempts, try again later";
        } else {
            String errorTxt = exception.getMessage();
            return errorTxt.equals("An internal error has occurred. [ 7: ]") ? "Network error, try again later" : errorTxt;
        }


    }
}
