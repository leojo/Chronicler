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
import project.service.globals.AbilityID;
import project.service.globals.SavingThrowID;

import javax.servlet.http.*;
import java.sql.SQLException;
import java.util.Arrays;


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

    @RequestMapping(value = "/myCharacters", method = RequestMethod.GET)
    public String myCharacters(Model model) {
        return "characters";
    }
    @RequestMapping(value = "/newCharacter", method = RequestMethod.GET)
    public String newCharacter(Model model, HttpSession session) {
        System.out.println("Prentaðu eitthvað annað svon þú sjjáir ég er að fara að prenta þetta");
        //System.out.println(Arrays.toString(session.getAttributeNames().toArray()));
        User user = (User)session.getAttribute("userId");
        if(user.getUserID().equals("andrea"))
            return "newcharacter";
        else
            return "loginFail";
    }
    @RequestMapping(value = "/myCampaigns", method = RequestMethod.GET)
    public String myCampaigns(Model model) {
        return "campaigns";
    }
    @RequestMapping(value = "/newCampaign", method = RequestMethod.GET)
    public String newCampaign(Model model) {
        return "newcampaign";
    }


    @RequestMapping(value = "/dummySheet", method = RequestMethod.GET)
    public String characterSheet(HttpSession session, Model sheetModel) {
        session.setAttribute("message", "Managed to get session access!");




        CharacterSheet c = new CharacterSheet();
        c.abilityScores.get(AbilityID.WIS).bonuses.put("Base Score", 14);
        c.classID.put(9, 10);
        c.classID.put(8, 1);
        c.update();


        sheetModel.addAttribute("classID", c.classID);
        sheetModel.addAttribute("raceID", c.raceID);
        sheetModel.addAttribute("hitPoints", c.currentHP);
        sheetModel.addAttribute("BAB", c.getBAB());
        sheetModel.addAttribute("saveFort", c.savingThrows.get(SavingThrowID.FORT).totalValue);
        sheetModel.addAttribute("saveWill", c.savingThrows.get(SavingThrowID.WILL).totalValue);
        sheetModel.addAttribute("saveReflex", c.savingThrows.get(SavingThrowID.REF).totalValue);
        sheetModel.addAttribute("name", c.name);

        return "index";
    }
}


