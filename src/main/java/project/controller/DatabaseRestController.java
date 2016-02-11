package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import project.persistence.account.Login;
import project.persistence.account.User;
import project.persistence.dbLookup.AccountStorage;
import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;
import project.persistence.dbRestUtils.Skill;
import project.persistence.enums.AbilityID;

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

    @RequestMapping("/skillData")
    public String skillData(){
        Lookup find = new Lookup();
        OfflineResultSet rs = find.skillData();
        HashMap<String, Skill> skillData = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        if(rs == null) return "Error fetching data!";
        rs.beforeFirst();
        while(rs.next()){
            String name = rs.getString("name");
            AbilityID abilityID = AbilityID.fromString(rs.getString("key_ability"));
            String action = rs.getString("action");
            String tryAgain = rs.getString("try_again");
            String special = rs.getString("special");
            String synergy = rs.getString("synergy");
            boolean trained = rs.getString("trained").equalsIgnoreCase("yes");
            boolean armor_check = rs.getString("armor_check").equalsIgnoreCase("yes");
            String description = rs.getString("skill_check");
            Skill s = new Skill(name, abilityID, trained, armor_check, description, synergy, action, tryAgain, special);
            skillData.put(name, s);
        }
        try {
            return mapper.writeValueAsString(skillData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    public class Echo{
        public String[] input;

        public Echo(String... strings){
            this.input = strings;
        }
    }
}
