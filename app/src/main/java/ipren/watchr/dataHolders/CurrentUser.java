package ipren.watchr.dataHolders;

import android.net.Uri;

import java.util.HashMap;

public class CurrentUser extends User {

    HashMap<String, String[]> movieLists;
    private FireRating[] ratings;
    private FireComment[] comments;

    public CurrentUser(String userName, String email, Uri userProfilePictureUri, String UID, boolean isVerified) {
        super(userName, email, userProfilePictureUri, UID, isVerified);
    }

    public CurrentUser(User user, FireRating[] ratings, FireComment[] comments, HashMap<String, String[]> movieLists) {
        super(user);
        this.ratings = ratings;
        this.comments = comments;
        this.movieLists = movieLists;

    }

    public FireComment[] getComments() {
        if (comments == null)
            return new FireComment[0];
        return comments.clone();
    }

    public FireRating[] getRatings() {
        if (ratings == null)
            return new FireRating[0];
        return ratings.clone();
    }

    public String[] getMovieList(String listName) {
        if (movieLists == null)
            return new String[0];
        if (movieLists.containsKey(listName))
            return movieLists.get(listName);

        return new String[0];
    }

}
