package ipren.watchr.dataHolders;

import com.google.firebase.firestore.DocumentId;

public class Rating {
    @DocumentId
    private String uID = "";
    private String movie_id = "";
    private String user_id = "";
    private int score = 0;

    //This is needed to enable firebase to initiate the object
    public Rating(){}

    public Rating(String movie_id, String user_id, String uID, int score) {
        if (movie_id != null)
            this.movie_id = movie_id;
        if (user_id != null)
            this.user_id = user_id;
        if (uID != null)
            this.uID = uID;
        this.score = score;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getuID() {
        return uID;
    }

    public int getScore(){return score;}
}
