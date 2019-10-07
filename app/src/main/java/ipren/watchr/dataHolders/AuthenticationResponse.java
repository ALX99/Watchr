package ipren.watchr.dataHolders;

public class AuthenticationResponse {
    private boolean successful;
    private String errorMsg = "";

    public AuthenticationResponse(boolean successful, String errorMsg) {
        this(successful);
        this.errorMsg = errorMsg;
    }

    public AuthenticationResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful(){
        return successful;
    }
    public String getErrorMsg(){
        return errorMsg;
    }

}
