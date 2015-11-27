package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.character.CharacterSheet;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.Lookup;
import project.service.spell.SpellList;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by andrea on 25.11.2015.
 */
@Controller
public class MyCharactersController {

    AccountStorage storage;
    // -----------------------------------------------
    // NEW CHAR STUFF -> USED FOR CLASS/RACE SELECTION
    // -----------------------------------------------
    @RequestMapping(value = "/myCharacters", method = RequestMethod.GET)
    public String listCharacters(Model model, HttpSession session) {

        User user = (User)session.getAttribute("userId");
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

    @RequestMapping(value = "/character{charID}", method = RequestMethod.GET)
    public String openCharacter(@PathVariable int charID, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);
        System.out.println("URL PARAMETER "+charID);
        storage = new AccountStorage("data/userAccounts.sqlite");
        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        CharacterBean charbean;
        CharacterSheet charSheet;
        if(user.getUserID() != null) {
            charbean = new CharacterBean();
            charbean.setDatabaseID(charID);
            charbean = loadBeanFromJson(charbean, user.getUserID());
            charSheet = new CharacterSheet(charbean,false);
            model.addAttribute("spellList", charSheet.knownSpells.getSpells());
            model.addAttribute("spellSlots",charSheet.spellSlots.getSpellSlots());
            model.addAttribute("spellSlotTypes",charSheet.spellSlots.getSpellSlotTypes());
            model.addAttribute("character", charbean);
            session.setAttribute("currentCharID", charID);
            System.out.println("LOADED CHARACTER NUMBER "+charID+" and set his session attr as "+session.getAttribute("currentCharID"));
            return "characterSheet";
        } else
            return "loginFail";
    }

    @RequestMapping(value = "deleteCharacter{charID}", method = RequestMethod.GET)
    public String deleteCharacter(@PathVariable int charID, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

        System.out.println("WE PRESSED THAT BUTTON BOOM");
        System.out.println("WERE DELETING CHARACTER WITH ID "+charID);
        storage = new AccountStorage("data/userAccounts.sqlite");
        storage.deleteCharacter(charID);

        return "myCharacters";
    }

    public CharacterBean loadBeanFromJson(CharacterBean bean, String user) {
        ObjectMapper mapper = new ObjectMapper();
        AccountStorage db = new AccountStorage("data/userAccounts.sqlite");
        String charJSON = db.searchCharacter(bean.getDatabaseID(), user);
        System.out.println("This is the json we found");
        System.out.println(charJSON);
        try {
            System.out.println("reading into bean...");
            bean = mapper.readValue(charJSON, CharacterBean.class);
            System.out.println(bean.getName());
            System.out.println(bean.getClassName());

        } catch(Exception e) {
            System.out.println("Something went wrong parsing your character");
        }

        return bean;
    }
}
