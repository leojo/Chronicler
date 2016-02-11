package project.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.account.Login;
import project.persistence.account.User;
import project.persistence.dbLookup.AccountStorage;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
public class DatabaseRestController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Echo androidLoginGet(Model model, HttpSession session) {
        // Make sure user is who we think they are, and put character bean in our model.
        User user = (User)session.getAttribute("userId");
        if(user == null) model.addAttribute("user", new User());
        else model.addAttribute("user", user);
        return new Echo("GETREQUEST");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Echo androidLoginPost(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) throws SQLException {
        User user = new User(username, password);
        model.addAttribute("user", user);
        Login login = new Login();
        if(login.evalLogin(user)) {
            session.setAttribute("userId", user);
            return new Echo("Login", "Successful",username);
        } else {
            return new Echo("Failure", username);
        }
    }

    public class Echo{
        public String[] input;

        public Echo(String... strings){
            this.input = strings;
        }
    }
}
