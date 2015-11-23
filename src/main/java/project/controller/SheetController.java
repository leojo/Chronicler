package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.dbLookup.Lookup;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class SheetController {

        Lookup find;
// --------------
// NEW CHAR STUFF
// --------------
        @RequestMapping(value = "/newCharacter", method = RequestMethod.GET)
        public String newCharacter(Model model, HttpSession session) {
            find = new Lookup();
            model.addAttribute("races", find.listRaces());
            model.addAttribute("classes", find.listClasses());

            model.addAttribute("charbean", new CharacterBean());

            User user = (User)session.getAttribute("userId");
            model.addAttribute("user", user);
            if(user.getUserID().equals("andrea"))
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
            model.addAttribute("user", user);
            model.addAttribute("character", charbean);

            if(user.getUserID().equals("andrea"))
                // TODO : Initialize character sheet from bean
                // TODO : Get bean from character sheet and update bean

                return "characterSheet";
            else
                return "loginFail";
        }

        @RequestMapping(value = "/myCharacters", method = RequestMethod.GET)
        public String myCharacters(Model model) {
            return "characters";
        }



}