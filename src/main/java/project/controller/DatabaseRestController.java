package project.controller;

import org.springframework.ui.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import project.persistence.account.Login;
import project.persistence.account.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import project.persistence.dbLookup.AccountStorage;
import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;
import project.persistence.dbRestUtils.Skill;
import project.persistence.enums.AbilityID;
import sun.misc.BASE64Decoder;

import java.util.HashMap;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
public class DatabaseRestController {

    AccountStorage find = new AccountStorage("data/userAccounts.sqlite");

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Echo androidLoginGet(Model model, HttpSession session) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if(user == null) model.addAttribute("user", new User());
        else model.addAttribute("user", user);
        return new Echo("GETREQUEST");
    }


    // TODO
    // Senda POST req  á /login með usrname og password,
    // senda til baka cookie með user ID
    // Cookie vistað í CookieStorage hjá notenda
    // GET req send með cookie parameternum, aðrir controllerar tékka hvort
    // um cookie matchar við einhvern notanda í gagnagrunni (ya?)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Echo androidLoginPost(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session, HttpServletResponse response) throws SQLException {
        User user = new User(username, password);
        model.addAttribute("user", user);
        Login login = new Login();
        if(login.evalLogin(user)) {
            session.setAttribute("userId", user);
            // Cookie user2 = new BASE64Decoder();
            Cookie userCookie = new Cookie("user", find.getUserCookie(user.getUserID()));
            userCookie.setMaxAge(60*60);
            //response.addCookie(userCookie);
            return new Echo("Login", "Successful",username);
        } else {
            return new Echo("Failure", username);
        }
    }
/*
    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    public Echo getCampaigns(@RequestParam("username")) {

    }

    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    public Echo getCharacters(@RequestParam("username")) {

    }
*/


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

    //TODO: Possibly move all this to it's own Controller
    @RequestMapping(value = "/campaignData", method = RequestMethod.GET)
    public String listCampaigns(@RequestParam("username") String username){
        /*Lookup find = new Lookup();
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
        }*/
        //TODO: retrieve user data from request JSON
        AccountStorage storage = new AccountStorage();
        String[] campaigns = storage.listPlayerCampaigns(username);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(campaigns);
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
