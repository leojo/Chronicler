package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.character.CharacterSheet;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.Lookup;
import project.service.item.SpecialItem;
import project.service.spell.Spell;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class SheetController {

    Lookup find;
    AccountStorage storage;
    // ---------------------------------------------
    // NEW CHARACTER - DOES NOT BELONG TO A CAMPAIGN
    // ---------------------------------------------
    @RequestMapping(value = "/newCharacter", method = RequestMethod.GET)
    public String newCharacter(Model model, HttpSession session) {
        find = new Lookup();
        model.addAttribute("races", find.listRaces());
        model.addAttribute("classes", find.listClasses());

        model.addAttribute("charbean", new CharacterBean());

        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        if(user.getUserID() != null)
            return "newcharacter";
        else
            return "loginFail";
    }


    // Post: This function initializes a character sheet based on some table values
    // for the class of charbean. This site should only be loaded when character bean has a class.
    @RequestMapping(value = "/newCharacter", method = RequestMethod.POST)
    public String newCharacterPost(@ModelAttribute CharacterBean charbean, Model model, HttpSession session) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);


        if(!charbean.getClassName().equals("") && !charbean.getRace().equals("")) {
            // TODO : Initialize character sheet from bean
            // TODO : Get bean from character sheet and update bean
            CharacterSheet cs = new CharacterSheet(charbean, true);
            charbean = cs.getBean();
            model.addAttribute("spellList", cs.knownSpells.getSpells());
            model.addAttribute("spellSlots",cs.spellSlots.getSpellSlots());
            model.addAttribute("spellSlotTypes",cs.spellSlots.getSpellSlotTypes());
            model.addAttribute("character", charbean);
            session.setAttribute("charbean", charbean);
            //session.setAttribute("characterSheet", cs);
            //cs = session.getAttribute("characterSheet");
            try {
                charbean.saveAsJson(user.getUserID());
                session.setAttribute("currentCharID", charbean.getDatabaseID());

            }catch(com.fasterxml.jackson.core.JsonProcessingException e) {
                System.out.println("Sadly we couldn't save your character, this is disastrous.");
            }

            return "characterSheet";
        } else return "loginFail";
    }

    // -----------------------------------------
    // NEW CHARACTER - DOES BELONG TO A CAMPAIGN
    // -----------------------------------------
    @RequestMapping(value = "/newCharacter{campID}", method = RequestMethod.GET)
    public String newCharacterCampaign(Model model, @PathVariable int campID, HttpSession session) {
        find = new Lookup();
        model.addAttribute("races", find.listRaces());
        model.addAttribute("classes", find.listClasses());
        model.addAttribute("campaignID", campID);
        model.addAttribute("charbean", new CharacterBean());

        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        if(user.getUserID() != null)
            return "newcharacter";
        else
            return "loginFail";
    }

    // Post: This function initializes a character sheet based on some table values
    // for the class of charbean. This site should only be loaded when character bean has a class.
    @RequestMapping(value = "/newCharacter{campID}", method = RequestMethod.POST)
    public String newCharacterCampaignPost(@ModelAttribute CharacterBean charbean, Model model, HttpSession session, @PathVariable int campID) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);


        if(!charbean.getClassName().equals("") && !charbean.getRace().equals("")) {
            // TODO : Initialize character sheet from bean
            // TODO : Get bean from character sheet and update bean
            CharacterSheet cs = new CharacterSheet(charbean, true);
            charbean = cs.getBean();
            model.addAttribute("character", charbean);
            session.setAttribute("charbean", charbean);
            session.setAttribute("characterSheet", cs);
            //cs = session.getAttribute("characterSheet");
            try {
                charbean.saveAsJson(user.getUserID());
                charbean.putInCampaign(campID);
                session.setAttribute("currentCharID", charbean.getDatabaseID());

            }catch(com.fasterxml.jackson.core.JsonProcessingException e) {
                System.out.println("Sadly we couldn't save your character, this is disastrous.");
            }

            return "characterSheet";
        } else return "loginFail";
    }


    // -----------------------------------------------------
    // EXISTING CHARACTER: LOAD - LEVELUP - UPDATE - DELETE
    // -----------------------------------------------------
    @RequestMapping(value = "/character{charID}", method = RequestMethod.GET)
    public String openCharacter(@PathVariable int charID, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        storage = new AccountStorage("data/userAccounts.sqlite");
        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        CharacterBean charbean;
        CharacterSheet charSheet;
        if(user.getUserID() != null) {
            charbean = new CharacterBean();
            charbean.setDatabaseID(charID);
            charbean = loadBeanFromJson(charbean, user.getUserID());
            session.setAttribute("charbean", charbean);
            charSheet = new CharacterSheet(charbean,false);
            model.addAttribute("spellList", charSheet.knownSpells.getSpells());
            model.addAttribute("spellSlots",charSheet.spellSlots.getSpellSlots());
            model.addAttribute("spellSlotTypes",charSheet.spellSlots.getSpellSlotTypes());
            model.addAttribute("character", charbean);
            session.setAttribute("currentCharID", charID);
            return "characterSheet";
        } else
            return "loginFail";
    }

    @RequestMapping(value = "/updateCharacter", method = RequestMethod.POST)
    public String myCharacterPost(@ModelAttribute CharacterBean charbean, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        CharacterBean oldBean = (CharacterBean)session.getAttribute("charbean");

        charbean.setSpellSlots_details(oldBean.getSpellSlots_details());
        CharacterSheet cs = new CharacterSheet(charbean,false);
        charbean = cs.getBean();
        model.addAttribute("user", user);
        model.addAttribute("character", charbean);
        model.addAttribute("characterSheet", cs);
        try {
            if(session.getAttribute("currentCharID") != null) charbean.setDatabaseID((int)session.getAttribute("currentCharID"));
            charbean.updateJson(user.getUserID());
        } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            System.out.println("Sadly we couldn't save your character, this is disastrous.");
        }
        return "characterSheet";
    }



    @RequestMapping(value = "levelUp{charID}_{classID}", method = RequestMethod.GET)
    public String levelUp(@PathVariable int charID, @PathVariable int classID, Model model, HttpSession session){
        System.out.println("Level up!!!");
        System.out.println("leveling up character "+charID+" one level in class "+classID);

        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        storage = new AccountStorage("data/userAccounts.sqlite");
        Lookup find = new Lookup();
        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        CharacterBean charbean;
        CharacterSheet charSheet;
        if(user.getUserID() != null) {
            charbean = new CharacterBean();
            charbean.setDatabaseID(charID);
            charbean = loadBeanFromJson(charbean, user.getUserID());
            session.setAttribute("charbean", charbean);
            charSheet = new CharacterSheet(charbean,false);
            charSheet.levelUp(classID);
            charbean = charSheet.getBean();
            try {
                charbean.updateJson(user.getUserID());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            model.addAttribute("spellList", charSheet.knownSpells.getSpells());
            model.addAttribute("spellSlots",charSheet.spellSlots.getSpellSlots());
            model.addAttribute("spellSlotTypes",charSheet.spellSlots.getSpellSlotTypes());
            model.addAttribute("character", charbean);
            session.setAttribute("currentCharID", charID);
            return "characterSheet";
        } else
            return "loginFail";
    }



    @RequestMapping(value = "rest{charID}", method = RequestMethod.GET)
    public String rest(@PathVariable int charID, Model model, HttpSession session){
        System.out.println("ZZZ...");

        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        storage = new AccountStorage("data/userAccounts.sqlite");
        Lookup find = new Lookup();
        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        CharacterBean charbean;
        CharacterSheet charSheet;
        if(user.getUserID() != null) {
            charbean = new CharacterBean();
            charbean.setDatabaseID(charID);
            charbean = loadBeanFromJson(charbean, user.getUserID());
            session.setAttribute("charbean", charbean);
            charSheet = new CharacterSheet(charbean,false);
            charSheet.rest();
            charbean = charSheet.getBean();
            try {
                charbean.updateJson(user.getUserID());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            model.addAttribute("spellList", charSheet.knownSpells.getSpells());
            model.addAttribute("spellSlots",charSheet.spellSlots.getSpellSlots());
            model.addAttribute("spellSlotTypes",charSheet.spellSlots.getSpellSlotTypes());
            model.addAttribute("character", charbean);
            session.setAttribute("currentCharID", charID);
            return "characterSheet";
        } else
            return "loginFail";
    }

    @RequestMapping(value = "deleteCharacter{charID}", method = RequestMethod.GET)
    public String deleteCharacter(@PathVariable int charID, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        storage = new AccountStorage("data/userAccounts.sqlite");
        storage.deleteCharacter(charID);

        return "myCharacters";
    }

    // ----------------------------------------------------
    // SPELL SLOT STUFF - MAYBE PUT IN SEPARATE CONTROLLER?
    // ----------------------------------------------------


    @RequestMapping(value = "/updateSpellslot", method = RequestMethod.POST)
    public String updateSpellslots(@RequestParam Map<String, String> allRequestParams, @ModelAttribute CharacterBean charbean, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        CharacterBean oldBean = (CharacterBean)session.getAttribute("charbean");
        charbean.setSpellSlots_details(oldBean.getSpellSlots_details());

        CharacterSheet cs = new CharacterSheet(charbean,false);
        ArrayList<String> newSpellInfo = new ArrayList<String>();
        for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
            if(entry.getKey().startsWith("spellID")){
                String spellSlotID = entry.getKey().substring(8);
                Integer level = Integer.parseInt(spellSlotID.split("_")[0]);
                Integer slotNum = Integer.parseInt(spellSlotID.split("_")[1]);
                Integer spellID = Integer.parseInt(entry.getValue());
                newSpellInfo.add(level+":"+slotNum+":"+spellID);
            }
        }
        cs.spellSlots.updateSpells(newSpellInfo);
        charbean = cs.getBean();

/*

        for (Map.Entry<String, String> entry : allRequestParams.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
*/


        model.addAttribute("user", user);
        model.addAttribute("characterSheet", cs);

        model.addAttribute("character", charbean);

        try {
            if(session.getAttribute("currentCharID") != null) charbean.setDatabaseID((int)session.getAttribute("currentCharID"));
            charbean.updateJson(user.getUserID());
        } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            System.out.println("Sadly we couldn't save your character, this is disastrous.");
        }
        return "characterSheet";
    }

    // ------------------
    // CHARACTER LISTINGS
    // ------------------

    @RequestMapping(value = "/myCharacters", method = RequestMethod.GET)
    public String listCharacters(Model model, HttpSession session) {

        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);

        storage = new AccountStorage("data/userAccounts.sqlite");
        HashMap<Integer, String> characters = storage.listCharacters(user.getUserID());
        if(characters == null) return new SheetController().newCharacter(model,session);
        model.addAttribute("myChars", characters);
        model.addAttribute("character", new CharacterBean());
        if(user.getUserID() != null)
            return "myCharacters";
        else
            return "loginFail";
    }

    // ------------------------------
    // CHARACTER SHEET UTIL FUNCTIONS
    // ------------------------------
    public CharacterBean loadBeanFromJson(CharacterBean bean, String user) {
        ObjectMapper mapper = new ObjectMapper();
        AccountStorage db = new AccountStorage("data/userAccounts.sqlite");
        String charJSON = db.searchCharacter(bean.getDatabaseID(), user);
        try {
            bean = mapper.readValue(charJSON, CharacterBean.class);

        } catch(Exception e) {
            System.out.println("Something went wrong parsing your character");
        }

        return bean;
    }


}
