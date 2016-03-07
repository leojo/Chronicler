package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
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
import project.persistence.enums.ArmorType;
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

    @RequestMapping(value = "/storeChar", method = RequestMethod.POST)
    public String storeChar(HttpServletRequest req){
        /*String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";*/
        String userID = "andrea";

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
            JSONObject character = new JSONObject(charJSON);

            AccountStorage storage = new AccountStorage("data/userAccounts.sqlite");
            int res = storage.addCharacterJSON(userID,charJSON,character.getString("name"));
            System.out.println("Return message from updateRaw is "+res + " after adding "+character.getString("name")+" for user "+userID+" with JSON "+charJSON);
            return "Return message from updateRaw is "+res + " after adding "+character.getString("name")+" for user "+userID+" with JSON "+charJSON;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Received invalid JSON";
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

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(@RequestParam("s") String searchString){
        HashMap<String,ArrayList<Item>> items = new HashMap<>();
        Lookup find = new Lookup();
        OfflineResultSet ors = find.mundaneItem(searchString);
        if(ors == null){
            return "No items matched your criteria";
        }
        ors.beforeFirst();
        while(ors.next()){
            Item item;
            String name, category;
            name = ors.getString("name");
            String family = ors.getString("family");
            if(family.equalsIgnoreCase("weapons")){
                String subCat = ors.getString("subcategory").toLowerCase();
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
                    category = "projectile";
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

                    category = "weapon";
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
                armorShield.setType(ArmorType.valueOf(ors.getString("subcategory").split(" ")[0]));

                // General Equipment stuff
                armorShield.setDescription(ors.getString("full_text"));

                category = "armorShield";
                item = armorShield;
            }
            else{
                MundaneItem mundaneItem = new MundaneItem();

                mundaneItem.setDescription(ors.getString("full_text"));

                category = "mundaneItem";
                item = mundaneItem;
            }

            // General Item stuff
            item.setName(name);
            item.setCost(ors.getString("cost"));
            item.setWeight(ors.getString("weight"));

            if(!items.containsKey(category)) items.put(category,new ArrayList<>());
            items.get(category).add(item);
        }
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
/*
    @RequestMapping(value = "/campaignData", method = RequestMethod.POST)
    public Echo postCampaign(@RequestParam("campaign_name") String campaignName, Model model, HttpSession session, HttpServletResponse response) {
        Log log = new SimpleLog("logger");
        log.info(session.toString());
        String userID = userIdFromCookie(response.getHeader("Cookie"));

        System.out.println("userID: " + userID + " \ncampaign name: " + campaignName);

        int res = find.insertCampaign(userID, campaignName);

        return new Echo("Database operation completed with code: "+res);
    }
*/

    @RequestMapping(value = "/campaignData", method = RequestMethod.POST)
    public String postCampaign(@RequestParam("campaign_name") String campaignName, HttpServletRequest req) {
        String userID = userIdFromCookie(req.getHeader("Cookie"));
        if(userID == null) return "Please log in";

        //Log log = new SimpleLog("logger");
        //log.info(session.toString());
        //String userID = userIdFromCookie(req.getHeader("Cookie"));

        //System.out.println("userID: " + userID + " \ncampaign name: " + campaignName);

        int res = find.insertCampaign(userID, campaignName);

        return  "Return message from updateRaw is "+res + " after adding "+campaignName+" for user "+userID+" (with  no JSON) ";
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
