package project.service.account;

import project.service.dbLookup.Lookup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by andrea on 28.10.2015.
 */
public class Login {

    private Lookup find;

    public Login() {
        this.find = new Lookup("data/userAccounts.sqlite");
    }

    public boolean evalLogin(User userInfo) throws SQLException {
        System.out.println("about to evaluate the login!");
        System.out.println("user ID:"+userInfo.getUserID());
        ResultSet rs = find.searchUser(userInfo.getUserID());
        if(rs == null) {
            System.out.println("result set empty, no user with id "+userInfo.getUserID());
            return false;
        } else if(!rs.getString("password").equals(userInfo.getPassword())) {
            System.out.println("Wrong password");
            return false;
        }
        System.out.println("Everything seems fine");
        return true;
    }
}
