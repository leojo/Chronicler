package project.service.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import project.service.character.CharacterBean;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        System.out.println("about to evaluate the login!");
        System.out.println("user ID:"+userInfo.getUserID());
        OfflineResultSet rs = find.searchUser(userInfo.getUserID());
        if(rs == null) {
            System.out.println("result set empty, no user with id "+userInfo.getUserID());
            return false;
        }
        rs.first();
        if(BCrypt.checkpw(userInfo.getPassword(), rs.getString("Password"))) {
            System.out.println("Success!");
            return true;
        }


        System.out.println("That's the wrong number");
        return false;
    }

    public static String encrypt(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(10, new SecureRandom()));
    }

/*
    public CharacterBean getCharacter() throws Exception{
        String jsonCharacter = find.searchCharacter("Nyx", "andrea");
        CharacterBean cb = mapper.readValue(jsonCharacter, CharacterBean.class);
        System.out.println("Working?");
        System.out.println(cb.getMaxHp());
        return cb;
    }*/
}
