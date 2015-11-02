package project.service.account;

import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by andrea on 28.10.2015.
 */
public class User {
    private String userID, password; //remember to encrypt and stuff later on

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        System.out.println("IS THIS TRIGGERING? What is the userID? "+userID);
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
