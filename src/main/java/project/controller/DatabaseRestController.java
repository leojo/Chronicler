package project.controller;

import org.springframework.ui.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import project.persistence.account.Login;
import project.persistence.account.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import project.persistence.dbLookup.AccountStorage;
import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;
import project.persistence.dbRestUtils.Skill;
import project.persistence.enums.AbilityID;
import project.persistence.spell.Spell;

import java.util.ArrayList;
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
            // If the database doesn't hold any cookie, update it:
            if(find.getUserCookie(user.getUserID()) == null) find.updateUserCookie(user.getUserID());
            Cookie userCookie = new Cookie("user", find.getUserCookie(user.getUserID()));
            userCookie.setMaxAge(60*60);
            response.addCookie(userCookie);
            return new Echo("Login", "Successful",username);
        } else {
            return new Echo("Failure", username);
        }
    }
/*
    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    public Echo getDMedCampaigns(@RequestParam("username")) {

    }
*/
    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    public String getCharacters(HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        HashMap<Integer, String> chars = find.listCharacters(userID);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(chars);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }



    @RequestMapping(value = "/skillData", method = RequestMethod.GET)
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

    @RequestMapping(value = "/spell", method = RequestMethod.GET)
    public String spell(@RequestParam("s") String searchString){
        ArrayList<Spell> spells = new ArrayList<>();
        Lookup find = new Lookup();
        OfflineResultSet ors = find.spell(searchString);
        ors.beforeFirst();
        while(ors.next()){
            spells.add(new Spell(ors));
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(spells);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    @RequestMapping(value = "/campaignData", method = RequestMethod.GET)
    public String listCampaigns(HttpServletRequest req){
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        HashMap<Integer, String> DMcampaigns = find.getDMedCampaigns(userID);
        HashMap<Integer, String> PCcampaigns = find.getPlayerCampaigns(userID);

        ArrayList<HashMap<Integer, String>> campaigns = new ArrayList<>();
        campaigns.add(DMcampaigns);
        campaigns.add(PCcampaigns);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(campaigns);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    @RequestMapping(value = "/campaignData", method = RequestMethod.POST)
    public Echo postCampaign(HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        String campaignName = req.getHeader("CampaignName");

        int res = find.insertCampaign(userID, campaignName);

        return new Echo("Database operation completed with code: "+res);
    }

    // A function to filter the user cookie ID from a cookie in the cookie header.
    // Will be used to identify the user when requesting data.
    public String userIdFromCookie(String cookieHeader) {
        String[] Cookies = cookieHeader.split(";");
        String cookieID = null;
        for(String val : Cookies) {
            if(val.contains("user")) {
                cookieID = val.split("=")[1];
            }
        }
        if(cookieID == null) return null;
        return find.matchUserByCookie(cookieID);
    }

    public class Echo{
        public String[] input;
        public Echo(String... strings){
            this.input = strings;
        }
    }
}
