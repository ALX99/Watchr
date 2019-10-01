package ipren.watchr.dataHolders;

public class Comment {
    private final int movieID;
    private String profilePicLink;
    private String username;
    private String comment;

    public Comment(int movieID, String profilePicLink, String username, String comment) {
        this.movieID = movieID;
        this.profilePicLink = profilePicLink;
        this.username = username;
        this.comment = comment;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
