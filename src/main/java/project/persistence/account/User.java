package project.persistence.account;

/**
 * Created by andrea on 28.10.2015.
 */
public class User {
    private String userID, password; //remember to encrypt and stuff later on

    public User(String userID,String password){
        this.userID = userID;
        this.password = password;
    }

    public User(){
        this.userID=null;
        this.password="";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
