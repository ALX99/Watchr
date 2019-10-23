package ipren.watchr.dataHolders;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;



import java.sql.Time;
import java.util.Date;


public class Comment {
    @DocumentId
    private String uID;
    private String movie_id;
    private String user_id;
    private String text;
    private Date date_created;

    //This is needed to enable firebase to initiate the object
    public Comment() {
    }

    public Comment(String movie_id, String user_id, String text, String uID, Date date_created) {
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.text = text;
        this.uID = uID;
        this.date_created = date_created;
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

    public Date getDate_created() { return date_created;}

}