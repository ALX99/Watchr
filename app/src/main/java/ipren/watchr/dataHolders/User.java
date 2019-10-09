package ipren.watchr.dataHolders;

import android.graphics.Bitmap;
import android.net.Uri;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;


//Immutable User object
public class User {
    private String userName = "No user name";
    private String email = "No email";
    private boolean isVerified = false;
    private String UID;
    private Uri userProfilePictureUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);

    // This object allows userProfilePictureUri = null, only for testing
    // once repository is enabled this will be removed
    public User() {
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User(String userName, String email, Uri userProfilePictureUri) {
        this(userName, email);
        this.userProfilePictureUri = userProfilePictureUri;
    }

    public User(String userName, String email, String UID, boolean isVerified, Uri userProfilePictureUri){
        this(userName,email, UID, isVerified);
        this.userProfilePictureUri = userProfilePictureUri;
    }

    public User(String userName, String email, String UID, boolean isVerified){
        this(userName,email);
        this.UID = UID;
        this.isVerified = isVerified;
    }
    public String getUID(){return UID;}

    public boolean isVerified(){return  isVerified; }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Uri getUserProfilePictureUri() {
        return userProfilePictureUri;
    }
}
