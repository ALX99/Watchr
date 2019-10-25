package ipren.watchr.dataholders;

import android.net.Uri;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;


//Immutable User object holding data for an authenticated user. This class sets default values important for all classes that uses the user application
public class User {
    private String userName = ""; //The authenticated users username
    private String email = ""; //The authenticated users email
    private boolean isVerified; //Shows weather or not the
    private String UID = "UID missing"; // The users unique ID
    private Uri userProfilePictureUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo); // The users profilePic may never be null. The default variable points to a local image.

    //Null safe constructor, it is important that none of these fields are set to null, and that a userProfilePic is always present.
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
