package ipren.watchr.dataholders;

import com.google.firebase.firestore.DocumentId;
//An immutable class representing a rating made by a user on a movie
public class Rating {
    @DocumentId //FireBase uses this annotation to identify a variable that should hold document ID
    private String uID = ""; // Uniqe ID of comment
    private String movie_id = ""; //Movie comment was made on
    private String user_id = ""; // User who made the comment
    private int score = 0; //The score the user left

    //This is needed to enable firebase to initiate the object
    public Rating(){}
    //Null safe constructor
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
