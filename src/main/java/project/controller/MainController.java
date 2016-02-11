package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.account.Login;
import project.persistence.account.User;
import project.persistence.dbLookup.AccountStorage;

import javax.servlet.http.*;
import java.sql.SQLException;


/**
 * Created by andrea on 19.10.2015.
 */
@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model, HttpSession session) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if(user == null) model.addAttribute("user", new User());
        else model.addAttribute("user", user);
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String loginPost(@ModelAttribute User user, Model model, HttpSession session) throws SQLException{
        model.addAttribute("user", user);
        Login login = new Login();
        if(login.evalLogin(user)) {
            session.setAttribute("userId", user);
            return "index";
        } else {
            return "loginFail";
        }
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute User user, Model model, HttpSession session) throws SQLException {
        model.addAttribute("user", user);
        user.setPassword(Login.encrypt(user.getPassword()));
        AccountStorage storage = new AccountStorage("data/userAccounts.sqlite");
        if (storage.searchUser(user.getUserID()) != null) {
            return "registerFail";
        }

        storage.addUser(user.getUserID(), user.getPassword());
        session.setAttribute("userId", user);

        return "index";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpSession session) {
        session.removeAttribute("userId");
        return "redirect:/";
    }

    @RequestMapping(value="/about", method = RequestMethod.GET)
    public String about(Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if(user == null) model.addAttribute("user", new User());
        else model.addAttribute("user", user);

        return "about";
    }



}


