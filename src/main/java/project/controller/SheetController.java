package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.character.CharacterSheet;
import project.service.dbLookup.Lookup;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class SheetController {

        Lookup find;

        // -----------------------------------------------
        // NEW CHAR STUFF -> USED FOR CLASS/RACE SELECTION
        // -----------------------------------------------
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


            if(!charbean.getClassName().equals("") && !charbean.getRace().equals("")) {
                // TODO : Initialize character sheet from bean
                // TODO : Get bean from character sheet and update bean
                CharacterSheet cs = new CharacterSheet(charbean, true);
                //charbean = cs.getBean();
                model.addAttribute("character", charbean);
                session.setAttribute("charbean", charbean);
                //session.setAttribute("characterSheet", cs);
                //cs = session.getAttribute("characterSheet");
                return "characterSheet";
            } else return "loginFail";
        }

        @RequestMapping(value = "/character", method = RequestMethod.GET)
        public String myCharacterGet(Model model) {
            //model.addAttribute()

            return "characterSheet";
        }

        @RequestMapping(value = "/updateCharacter", method = RequestMethod.POST)
        public String myCharacterPost(@ModelAttribute CharacterBean charbean, Model model, HttpSession session) {
            User user = (User)session.getAttribute("userId");
            model.addAttribute("user", user);
            model.addAttribute("character", charbean);
            System.out.println("Alrighty then, here is our name");
            System.out.println(charbean.getName());
            System.out.println("And our class");
            System.out.println(charbean.getClassName());


            return "characterSheet";
        }
}
