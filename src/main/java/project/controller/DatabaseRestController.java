package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.persistence.account.Login;
import project.persistence.account.User;
import project.persistence.dbLookup.AccountStorage;
import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;
import project.persistence.dbRestUtils.*;
import project.persistence.enums.AbilityID;
import project.persistence.feat.Feat;
import project.persistence.spell.Spell;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
public class DatabaseRestController {

    AccountStorage find = new AccountStorage("data/userAccounts.sqlite");


    // DEPRECATED : Should be (safely) removed
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Echo androidLoginGet(Model model, HttpSession session) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if(user == null) model.addAttribute("user", new User());
        else model.addAttribute("user", user);
        return new Echo("GETREQUEST");
    }


    // Login post request, sends username and password -> Should hash password before sending over http.
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
            // We might want to make the cookie live longer?
            userCookie.setMaxAge(60*60);
            response.addCookie(userCookie);
            return new Echo("Login", "Successful",username);
        } else {
            return new Echo("Failure", username);
        }
    }

    // TODO: Finish implementing the register controller
    @RequestMapping(value = "/androidRegister", method = RequestMethod.POST)
    public Echo androidRegisterPost(@RequestParam("username") String username, @RequestParam("password") String password,HttpServletResponse response) throws SQLException {
        User user = new User(username, password);
        Login login = new Login();
        if(find.searchUser(user.getUserID()) == null) {
            // create the user
            find.addUser(user.getUserID(), Login.encrypt(user.getPassword()));
            // get that man a cookie!
            find.updateUserCookie(user.getUserID());
            Cookie userCookie = new Cookie("user", find.getUserCookie(user.getUserID()));
            userCookie.setMaxAge(60*60);
            response.addCookie(userCookie);
            return new Echo("Register", "Successful",username);
        } else {
            return new Echo("Register", "Failure", "A user with that name already exists", username);
        }
    }

    // Gets the list of characters for the user whose cookie was sent with the request
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
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

    // Gets the list of characters for the user whose cookie was sent with the request
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
    @RequestMapping(value = "/getCharacterJSON", method = RequestMethod.GET)
    public String getCharacter(HttpServletRequest req, @RequestParam("id") int charID) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        String charJSON = find.searchCharacter(charID, userID);

        return charJSON;
    }

    // Stores the character JSON sent with the request in the AccountStorage database, characters table,
    // so the character will have its' name and owner listed as well as the JSON string that
    // describes all aspects of the character. This JSON can be used by the android app to create a
    // character sheet for this character.
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
    @RequestMapping(value = "/storeChar", method = RequestMethod.POST)
    public String storeChar(HttpServletRequest req){
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        // A string builder and buffered reader is needed to read the
        // long input stream from the request and make a JSON string from it.
        String charJSON = "";
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = req.getReader();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        charJSON = sb.toString();
        try {
            // We can now convert the string to JSONObject and use it to get the character's name
            JSONObject character = new JSONObject(charJSON);

            AccountStorage storage = new AccountStorage("data/userAccounts.sqlite");
            // Store the character information in the database:
            int res = storage.addCharacterJSON(userID,charJSON,character.getString("name"));
            System.out.println("Return message from updateRaw is "+res + " after adding "+character.getString("name")+" for user "+userID+" with JSON "+charJSON);
            return "Return message from updateRaw is "+res + " after adding "+character.getString("name")+" for user "+userID+" with JSON "+charJSON.substring(0,100)+"...";
        } catch (ParseException e) {
            e.printStackTrace();
            return "Received invalid JSON";
        }
    }

    // A Controller to request a table of all skills (their names, descriptions, etc)
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

    // A controller to get a list of all available races. At the moment, there are only
    // 3 available races but we can hopefully add more to the database later on.
    @RequestMapping(value = "/raceList", method = RequestMethod.GET)
    public String raceList(){
        Lookup find = new Lookup();
        ArrayList<String> races = find.listRaces();
        try {
            return (new ObjectMapper()).writeValueAsString(races);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // A controller to get the list of available classes.
    @RequestMapping(value = "/classList", method = RequestMethod.GET)
    public String classList(){
        Lookup find = new Lookup();
        ArrayList<String> classes = find.listClasses();
        try {
            return (new ObjectMapper()).writeValueAsString(classes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // A controller to get the advancement table for a specified class
    @RequestMapping(value = "/classData", method = RequestMethod.GET)
    public String classData(@RequestParam("s") String className){
        Lookup find = new Lookup();
        OfflineResultSet ors = find.advTable(className);
        ArrayList<Integer> relevantColumns = new ArrayList<>();
        for(int i=3; i<ors.colCount(); i++) {
            ors.beforeFirst();
            boolean isRelevant = false;
            while (ors.next()) {
                if(!ors.getString(i).equalsIgnoreCase("none")) isRelevant = true;
            }
            if(isRelevant) relevantColumns.add(i);
        }

        ArrayList<String> colNames = new ArrayList<>();
        for(Integer colNum : relevantColumns){
            colNames.add(ors.getColName(colNum));
        }

        ArrayList<ArrayList<String>> tableData = new ArrayList<>();
        tableData.add(colNames);
        ors.beforeFirst();
        while (ors.next()){
            ArrayList<String> row = new ArrayList<>();
            for(Integer colNum : relevantColumns){
                row.add(ors.getString(colNum));
            }
            tableData.add(row);
        }

        try {
            return (new ObjectMapper()).writeValueAsString(tableData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // A controller to get a specific spell, searched by searchString s .
    @RequestMapping(value = "/spell", method = RequestMethod.GET)
    public String spell(@RequestParam("s") String searchString){
        ArrayList<Spell> spells = new ArrayList<>();
        Lookup find = new Lookup();
        OfflineResultSet ors = find.spell(searchString);
        if(ors == null){
            return "No spells matched your criteria";
        }
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

    // A controller to get a specific feat, searched by searchString s
    @RequestMapping(value = "/feat", method = RequestMethod.GET)
    public String feat(@RequestParam("s") String searchString){
        ArrayList<Feat> feats = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Lookup find = new Lookup();
        OfflineResultSet ors = find.feat(searchString);
        if(ors == null){
            return "No feats matched your criteria";
        }
        ors.beforeFirst();
        while(ors.next()){
            Feat newFeat = new Feat(ors);
            feats.add(newFeat);
        }
        try {
            return mapper.writeValueAsString(feats);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    // A controller to get a specific item, searched by searchString s
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(@RequestParam("s") String searchString){
        ArrayList<Item> items = new ArrayList<>();
        Lookup find = new Lookup();
        OfflineResultSet ors = find.mundaneItem(searchString);
        if(ors == null){
            return "No items matched your criteria";
        }
        ors.beforeFirst();
        while(ors.next()){
            Item item;
            String name;
            name = ors.getString("name");
            String family = ors.getString("family");
            // Categorize items to : weapons, armor/shields and mundane items
            if(family.equalsIgnoreCase("weapons")){
                String subCat = ors.getString("subcategory").toLowerCase();
                // Categorize weapon items by their type: ammunition or regular weapon.
                if(subCat.equalsIgnoreCase("ammunition")){
                    Projectile projectile = new Projectile();
                    String rawName = ors.getString("name");
                    name = rawName;
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(rawName.replaceAll("[^\\d.]", ""));
                    } catch (java.lang.NumberFormatException e){
                        System.err.println("unable to parse a number from string :\""+rawName.replaceAll("[^\\d.]", "")+"\"");
                    }
                    projectile.setQuantity(quantity);
                    item = projectile;
                } else {
                    Weapon weapon = new Weapon();
                    boolean twoHand, oneHand, ranged, thrown, light;
                    twoHand = subCat.startsWith("tw");
                    oneHand = subCat.startsWith("o");
                    ranged = subCat.startsWith("r");
                    thrown = subCat.startsWith("th");
                    light = subCat.startsWith("l");
                    weapon.setTwoHand(twoHand);
                    weapon.setOneHand(oneHand);
                    weapon.setRanged(ranged);
                    weapon.setThrown(thrown);
                    weapon.setLight(light);
                    weapon.setWepCat(ors.getString("category"));
                    weapon.setCrit(ors.getString("critical"));
                    weapon.setDamage(ors.getString("dmg_m"));
                    weapon.setDamageTypes(ors.getString("type"));
                    weapon.setRangeIncr(ors.getString("range_increment"));

                    // General Equipment stuff
                    weapon.setDescription(ors.getString("full_text"));

                    item = weapon;
                }
            }
            else if(family.equalsIgnoreCase("armor and shields")){
                ArmorShield armorShield = new ArmorShield();

                armorShield.setACbonus(ors.getString("armor_shield_bonus"));
                armorShield.setMaxDex(ors.getString("maximum_dex_bonus"));
                armorShield.setArcaneSpellFailure(ors.getString("arcane_spell_failure_chance"));
                armorShield.setArmorCheckPen(ors.getString("armor_check_penalty"));
                armorShield.setSpeed30(ors.getString("speed_30"));
                armorShield.setSpeed20(ors.getString("speed_20"));
                armorShield.setType(ors.getString("subcategory").split(" ")[0]);

                // General Equipment stuff
                armorShield.setDescription(ors.getString("full_text"));

                item = armorShield;
            }
            else{
                MundaneItem mundaneItem = new MundaneItem();

                mundaneItem.setDescription(ors.getString("full_text"));

                item = mundaneItem;
            }

            // General Item stuff
            item.setName(name);
            item.setCost(ors.getString("cost"));
            item.setWeight(ors.getString("weight"));

            items.add(item);
        }

        // TODO: implement this
        /*ors = find.specialItem(searchString);
        ors.beforeFirst();
        while(ors.next()){

        }*/

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    // Gets a list of all campaigns for a user.
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
    @RequestMapping(value = "/campaignData", method = RequestMethod.GET)
    public String listCampaigns(HttpServletRequest req){
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        HashMap<Integer, String> DMcampaigns = find.getDMedCampaigns(userID);
        HashMap<Integer, String> PCcampaigns = find.getPlayerCampaigns(userID);
        if (DMcampaigns == null) DMcampaigns = new HashMap<>();
        if (PCcampaigns == null) PCcampaigns = new HashMap<>();

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

    // Gets a list of all campaigns for a user.
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
    @RequestMapping(value = "/campaignDetails", method = RequestMethod.GET)
    public String getCampaignDetails(@RequestParam("campaign_name") String campaignName, HttpServletRequest req){
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        HashMap<Integer, String> characters = find.getCampaignPlayers(campaignName);
        if (characters == null) {
            characters = new HashMap<>();
        }

        ArrayList<HashMap<Integer, String>> campaigns = new ArrayList<>();
        campaigns.add(characters);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(campaigns);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    // For adding a campaign with name campaignName for the user with the matching cookie.
    // Will only respond to a request with a cookie that matches a cookie for some user
    // in the database.
    @RequestMapping(value = "/campaignData", method = RequestMethod.POST)
    public String postCampaign(@RequestParam("campaign_name") String campaignName, HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        int res = find.insertCampaign(userID, campaignName);
        return  "Return message from updateRaw is "+res + " after adding "+campaignName+" for user "+userID+" (with  no JSON) ";
    }

    @RequestMapping(value = "/respondToInvite", method = RequestMethod.POST)
    public String respondToInvite(@RequestParam("CampaignIndex") Integer index,
                                  @RequestParam(value = "Character", required = false) String character,
                                  HttpServletRequest req) {
        // If no character is specified, reject invite
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        int res;
        if(character == null) {
            res = find.removeInvite(userID, index);
        } else {
            res = find.acceptInvite(userID, character, index);
        }

        return  "Return message from updateRaw is "+res + " after responding to invite number "+index;
    }

    @RequestMapping(value = "/inviteToCampaign", method = RequestMethod.POST)
    public String inviteToCampaign(@RequestParam("Campaign") String campaign, @RequestParam("User") String user, HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        int res = find.inviteToCampaign(campaign, user);

        return  "Return message from updateRaw is "+res + " after inviting "+user+" to campaign "+campaign+"";
    }

    @RequestMapping(value = "/invites", method = RequestMethod.GET)
    public String listInvites(HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        OfflineResultSet invites = find.getInviteList(userID);

        ArrayList<String> inviteList = new ArrayList<>();

        String firstInvite;
        if (invites == null) {
            return "User not found";
        } else {
            invites.first();
            firstInvite = invites.getString("Invites");
            inviteList.add(firstInvite);
            while (invites.next()) {
                inviteList.add(invites.getString("Invites"));
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(inviteList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }


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

    // Generic object to 'echo' out as JSON, just for testing purposes
    public class Echo{
        public String[] input;
        public Echo(String... strings){
            this.input = strings;
        }
    }
}
