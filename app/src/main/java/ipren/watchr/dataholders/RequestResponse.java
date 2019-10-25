package ipren.watchr.dataholders;
//Immutable class, represents a response of any kind
public class RequestResponse {
    private boolean successful; //Was the request successful
    private String errorMsg = ""; //Provided errorMsg

    public RequestResponse(boolean successful, String errorMsg) {
        this(successful);
        if(errorMsg != null)
            this.errorMsg = errorMsg;
    }

    public RequestResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
