package ipren.watchr.dataHolders;

import android.graphics.Bitmap;


//Immutable User object
public class User {
    private String userName = "No user name";
    private String email = "No email";
    private Bitmap userProfilePicture;

    // This object allows userProfilePicture = null, only for testing
    // once repository is enabled this will be removed
    public User() {
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User(String userName, String email, Bitmap userProfilePicture) {
        this(userName, email);
        this.userProfilePicture = userProfilePicture;
    }


    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getUserProfilePicture() {
        return userProfilePicture;
    }
}
