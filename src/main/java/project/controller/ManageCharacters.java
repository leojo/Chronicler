package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String newCharacter(Model model, HttpSession session) {

        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

        storage = new AccountStorage("data/userAccounts.sqlite");

        model.addAttribute("myChars", storage.listCharacters(user.getUserID()));
        model.addAttribute("charbean", new CharacterBean());
        if(user.getUserID() != null)
            return "myCharacters";
        else
            return "loginFail";
    }
}
