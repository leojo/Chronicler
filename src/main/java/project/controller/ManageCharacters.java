package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.Lookup;

import javax.servlet.http.HttpSession;

/**
 * Created by andrea on 25.11.2015.
 */
@Controller
public class ManageCharacters {

    AccountStorage storage;
    // -----------------------------------------------
    // NEW CHAR STUFF -> USED FOR CLASS/RACE SELECTION
    // -----------------------------------------------
    @RequestMapping(value = "/myCharacters", method = RequestMethod.GET)
    public String listCharacters(Model model, HttpSession session) {

        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

        storage = new AccountStorage("data/userAccounts.sqlite");

        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        model.addAttribute("character", new CharacterBean());
        if(user.getUserID() != null)
            return "myCharacters";
        else
            return "loginFail";
    }

    @RequestMapping(value = "/character", method = RequestMethod.POST)
    public String openCharacter(@ModelAttribute CharacterBean charbean, Model model, HttpSession session) {

        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

        storage = new AccountStorage("data/userAccounts.sqlite");

        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));

        if(user.getUserID() != null) {
            charbean = loadBeanFromJson(charbean, user.getUserID());
            model.addAttribute("character", charbean);
            return "characterSheet";
        } else
            return "loginFail";
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
