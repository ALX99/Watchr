package ipren.watchr.dataHolders;

public class FireBaseResponse {
    private boolean successful;
    private String errorMsg = "";

    public FireBaseResponse(boolean successful, String errorMsg) {
        this(successful);
        this.errorMsg = errorMsg;
    }

    public FireBaseResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful(){
        return successful;
    }
    public String getErrorMsg(){
        return errorMsg;
    }

}
