package ipren.watchr.dataHolders;

import android.net.Uri;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;


//Immutable User object
public class User {
    private String userName = "No user name";
    private String email = "No email";
    private boolean isVerified;
    private String UID = "UID missing";
    private Uri userProfilePictureUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);


    public User(String userName, String email, Uri userProfilePictureUri, String UID, boolean isVerified) {
        if (userName != null)
            this.userName = userName;
        if (email != null)
            this.email = email;
        if (userProfilePictureUri != null)
            this.userProfilePictureUri = userProfilePictureUri;
        if (UID != null)
            this.UID = UID;
        this.isVerified = isVerified;
    }

    public User(User user) {
        this.userName = user.email;
        this.email = user.email;
        this.isVerified = user.isVerified;
        this.UID = user.UID;
        this.userProfilePictureUri = user.userProfilePictureUri;

    }


    public String getUID() {
        return UID;
    }

    public boolean isVerified() {
        return isVerified;
    }

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
