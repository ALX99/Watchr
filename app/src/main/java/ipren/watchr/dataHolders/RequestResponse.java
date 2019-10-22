package ipren.watchr.dataHolders;

public class RequestResponse {
    private boolean successful;
    private String errorMsg = "";

    public RequestResponse(boolean successful, String errorMsg) {
        this(successful);
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
