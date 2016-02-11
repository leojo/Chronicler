package project.controller;

import org.springframework.web.bind.annotation.*;
import project.persistence.account.Login;
import project.persistence.account.User;
import project.persistence.dbLookup.AccountStorage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
public class DatabaseRestController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object Login(@RequestParam("username") String username, @RequestParam("password") String password) {
        //return new Echo(username, password);
        Login l = new Login();
        AccountStorage search = new AccountStorage();
        User u = new User(username, password);
        if(l.evalLogin(u)){
            /*HashMap<Integer,String> characters = search.listCharacters(username);

            ArrayList<String> characterJSONs = new ArrayList<>();
            for(Integer key : characters.keySet()){
                String characterJSON = search.searchCharacter(key,username);
                characterJSONs.add(characterJSON);
            }*/
            return new Echo("Login", "Successful", username);
        } else {
            return new Echo("Failure", username, password);
        }
    }

    public class Echo{
        public String[] input;

        public Echo(String... strings){
            this.input = strings;
        }
    }
}
