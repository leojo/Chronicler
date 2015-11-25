package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.Login;
import project.service.account.User;

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

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        return "redirect:/";
    }

}


