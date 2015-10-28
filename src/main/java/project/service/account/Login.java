package project.service.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.service.character.CharacterBean;
import project.service.dbLookup.Lookup;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by andrea on 28.10.2015.
 */
public class Login {

    private Lookup find;
    private ObjectMapper mapper;

    public Login() {
        this.mapper = new ObjectMapper();
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

    public CharacterBean getCharacter() throws Exception{
        String jsonCharacter = find.searchCharacter("Nyx", "andrea");
        CharacterBean cb = mapper.readValue(jsonCharacter, CharacterBean.class);

        return cb;
    }
}
