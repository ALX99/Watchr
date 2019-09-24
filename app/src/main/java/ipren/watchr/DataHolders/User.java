package ipren.watchr.DataHolders;

import ipren.watchr.Activities.MainActivity;

//Immutable User object
public class User {
    private final String userName;

    public User(String userName){
        this.userName = userName;
    }
    public User(){
        this("No user name");
    }
    public String getUserName(){
        return userName;
    }
}
