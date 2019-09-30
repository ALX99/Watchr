package ipren.watchr.model;

import android.graphics.Bitmap;



//Immutable User object
public class User {
    private String userName = "No user name";
    private Bitmap userProfilePicture;

    public User(){};

    public User(String userName){
        this.userName = userName;
    }

    public User(String userName, Bitmap userProfilePicture){
        this(userName);
        this.userProfilePicture = userProfilePicture;
    }


    public String getUserName(){
        return userName;
    }
    public Bitmap getUserProfilePicture(){
        return userProfilePicture;
    }
}
