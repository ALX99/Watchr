package ipren.watchr.dataHolders;

import android.net.Uri;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;

public class PublicProfile {

    private Uri profilePhotoUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.default_profile_photo);
    private String username = "Not set";

    public PublicProfile() { }

    public PublicProfile(Uri profilePhotoUri, String username) {
        if (profilePhotoUri != null)
            this.profilePhotoUri = profilePhotoUri;
        if (username != null)
            this.username = username;

    }

    public String getUsername() {
        return username;
    }

    public Uri getProfilePhotoUri() {
        return profilePhotoUri;
    }


}
