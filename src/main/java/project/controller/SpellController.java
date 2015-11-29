package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.account.User;
import project.persistence.dbLookup.Lookup;
import project.persistence.spell.Spell;

import javax.servlet.http.HttpSession;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class SpellController {

        Lookup find;

    @RequestMapping(value = "/spell{spellID}", method = RequestMethod.GET)
    public String openCharacter(@PathVariable int spellID, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        Spell spell = new Spell(spellID);
        model.addAttribute(spell);
        return "spellDetails";
    }

}
