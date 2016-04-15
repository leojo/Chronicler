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
    public Response androidLoginPost(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session, HttpServletResponse response) throws SQLException {
        User user = new User(username, password);
        model.addAttribute("user", user);
        Login login = new Login();
        if(!login.userExists(user)) {
            return new Response("nouser", "This user does not exist. Do you wish to register this user?");
        }else if(login.evalLogin(user)) {
            session.setAttribute("userId", user);
            // If the database doesn't hold any cookie, update it:
            if(find.getUserCookie(user.getUserID()) == null) find.updateUserCookie(user.getUserID());
            Cookie userCookie = new Cookie("user", find.getUserCookie(user.getUserID()));
            // We might want to make the cookie live longer?
            userCookie.setMaxAge(60*60);
            response.addCookie(userCookie);
            return new Response("success", "");
        } else {
            return new Response("failure", "Wrong password");
        }
    }

    // TODO: Finish implementing the register controller
    @RequestMapping(value = "/androidRegister", method = RequestMethod.POST)
    public Response androidRegisterPost(@RequestParam("username") String username, @RequestParam("password") String password,HttpServletResponse response) throws SQLException {
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
            return new Response("success", "Registered user "+user);
        } else {
            return new Response("failure", "Failed to register user.");
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

    // Deletes a character
    @RequestMapping(value = "/deleteChar", method = RequestMethod.GET)
    public int deleteCharacter(HttpServletRequest req, @RequestParam("charID") int charID) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return 0;

        int res = find.deleteCharacter(charID);

        return res;
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
        HashMap<String,String> colNameReplace = new HashMap<>();

        //<editor-fold desc="Populate Replacements">
        String[] original = {"level","base_attack_bonus","fort_save","ref_save","will_save","caster_level","points_per_day","ac_bonus","flurry_of_blows","bonus_spells","powers_known","unarmored_speed_bonus","unarmed_damage","power_level","special","slots_0","slots_1","slots_2","slots_3","slots_4","slots_5","slots_6","slots_7","slots_8","slots_9","spells_known_0","spells_known_1","spells_known_2","spells_known_3","spells_known_4","spells_known_5","spells_known_6","spells_known_7","spells_known_8","spells_known_9"};
        String[] replacement = {"Level","Base Attack Bonus","Fortitude Save","Reflex Save","Will Save","Caster Level","Power Points per Day","AC Bonus","Flurry of Blows Attack Bonus","Bonus Spells","Powers Known","Unarmored Speed Bonus","Unarmed Damage","Maximum Power Level Known","Special","Spells per Day 0","Spells per Day 1st","Spells per Day 2nd","Spells per Day 3rd","Spells per Day 4th","Spells per Day 5th","Spells per Day 6th","Spells per Day 7th","Spells per Day 8th","Spells per Day 9th","Spells Known 0","Spells Known 1st","Spells Known 2nd","Spells Known 3rd","Spells Known 4th","Spells Known 5th","Spells Known 6th","Spells Known 7th","Spells Known 8th","Spells Known 9th"};
        for (int i = 0; i < original.length; i++) {
            colNameReplace.put(original[i],replacement[i]);
        }
        //</editor-fold>

        Lookup find = new Lookup();
        OfflineResultSet ors = find.advTable(className);
        ArrayList<String> relevantColumns = new ArrayList<>();
        for(int i=3; i<ors.colCount(); i++) {
            ors.beforeFirst();
            boolean isRelevant = false;
            while (ors.next()) {
                if(!ors.getString(i).equalsIgnoreCase("none")) isRelevant = true;
            }
            if(isRelevant) relevantColumns.add(ors.getColName(i));
        }

        ArrayList<String> properColNames = new ArrayList<>();
        for(String colName : relevantColumns){
            properColNames.add(colNameReplace.get(colName));
        }

        ArrayList<HashMap<String,String>> tableData = new ArrayList<>();
        HashMap<String,String> header = new HashMap<>();
        for(int i=0; i<relevantColumns.size(); i++){
            header.put(relevantColumns.get(i),properColNames.get(i));
        }
        tableData.add(header);
        ors.beforeFirst();
        while (ors.next()){
            HashMap<String,String> row = new HashMap<>();
            for(String colName : relevantColumns){
                String val = ors.getString(colName);
                val = val.equalsIgnoreCase("none")?"-":val;
                row.put(colName,val);
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

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> campaignInfo = new HashMap<>();
        try {
            campaignInfo.put("Players", mapper.writeValueAsString(characters));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        campaignInfo.put("Public Notes", find.getPublicNotes(find.getCampaignID(campaignName)).toString());
        campaignInfo.put("Private Notes", find.getPrivateNotes(find.getCampaignID(campaignName)).toString());
        campaignInfo.put("Journal Entries", find.getJournalEntries(find.getCampaignID(campaignName)).toString());

        try {
            return mapper.writeValueAsString(campaignInfo);
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
        public String[] message;
        public Echo(String... strings){
            this.message = strings;
        }
    }

    // Generic object to 'echo' out as JSON, for example
    // {"code": "success", "message":""}
    // or instead
    // {"code": "failure", "Username and password don't match"}
    public class Response {
        public String code = "";
        public String message = "";
        public Response(String c, String m){
            this.code = c;
            this.message = m;
        }
    }
}
