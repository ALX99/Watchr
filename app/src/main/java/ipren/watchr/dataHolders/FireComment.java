package ipren.watchr.dataHolders;

import android.net.Uri;

import com.google.firebase.firestore.DocumentId;

public class FireComment {
    @DocumentId
    private String uID;
    private String movie_id;
    private String user_id;
    private String text;

    //This is needed to enable firebase to initiate the object
    public FireComment() { }


    public FireComment(String movie_id, String user_id, String text, String uID) {
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.text = text;
        this.uID = uID;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getText() {
        return text;
    }

    public String getuID() {
        return uID;
    }

    public FireComment(String movie_id, String user_id, String text, String uID){
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.text = text;
        this.uID = uID;
    }
}
