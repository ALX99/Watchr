package ipren.watchr.dataHolders;

public class FireRating {
    private String movie_id = "";
    private String user_id = "";
    private String rating_id = "";

    public String getMovie_id() {
        return movie_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRating_id() {
        return rating_id;
    }

    public FireRating(String movie_id, String user_id, String rating_id) {
        if (movie_id != null)
            this.movie_id = movie_id;
        if (user_id != null)
            this.user_id = user_id;
        if (rating_id != null)
            this.rating_id = rating_id;
    }
}
