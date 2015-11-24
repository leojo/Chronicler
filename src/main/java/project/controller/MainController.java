package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.Login;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.character.CharacterSheet;
import project.service.dbLookup.Lookup;
import project.service.globals.AbilityID;
import project.service.globals.SavingThrowID;

import javax.servlet.http.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;


/**
 * Created by andrea on 19.10.2015.
 */
@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String loginPost(@ModelAttribute User user, Model model, HttpSession session) throws SQLException{
        model.addAttribute("user", user);
        Login login = new Login();
        if(login.evalLogin(user)) {
            session.setAttribute("userId", user);
            try {
                System.out.println("login successful, getting character");
                CharacterBean cb = login.getCharacter();
                model.addAttribute("character", cb);
                System.out.println(cb.getName());
            } catch(Exception e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
            return "index";
        } else {
            return "loginFail";
        }
    }

}


