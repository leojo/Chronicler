package project.persistence.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import project.persistence.dbLookup.AccountStorage;
import project.persistence.dbLookup.OfflineResultSet;

import java.security.SecureRandom;

/**
 * Created by andrea on 28.10.2015.
 */
public class Login {

    private AccountStorage find;
    private ObjectMapper mapper;

    public Login() {
        this.mapper = new ObjectMapper();
        this.find = new AccountStorage("data/userAccounts.sqlite");
    }

    public boolean evalLogin(User userInfo){
        System.out.println("user ID:"+userInfo.getUserID());
        OfflineResultSet rs = find.searchUser(userInfo.getUserID());
        if(rs == null) {
            System.out.println("result set empty, no user with id "+userInfo.getUserID());
            return false;
        }
        rs.first();
        if(BCrypt.checkpw(userInfo.getPassword(), rs.getString("Password"))) {
            return true;
        }


        System.out.println("Login failed");
        return false;
    }

    public static String encrypt(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(10, new SecureRandom()));
    }

}
