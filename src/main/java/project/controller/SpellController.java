package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.character.CharacterBean;
import project.service.character.CharacterSheet;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.Lookup;
import project.service.spell.Spell;

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
        model.addAttribute("user", user);
        Spell spell = new Spell(spellID);
        model.addAttribute(spell);
        return "spellDetails";
    }


    @RequestMapping(value = "/updateSpellslot", method = RequestMethod.POST)
    public String myCharacterPost(@ModelAttribute CharacterBean charbean, Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        CharacterBean oldBean = (CharacterBean)session.getAttribute("charbean");
        charbean.setSpellSlots_details(oldBean.getSpellSlots_details());
        CharacterSheet cs = new CharacterSheet(charbean,false);
        charbean = cs.getBean();
        model.addAttribute("user", user);
        model.addAttribute("character", charbean);
        model.addAttribute("characterSheet", cs);
        try {
            if(session.getAttribute("currentCharID") != null) charbean.setDatabaseID((int)session.getAttribute("currentCharID"));
            charbean.updateJson(user.getUserID());
        } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
            System.out.println("Sadly we couldn't save your character, this is disastrous.");
        }
        return "characterSheet";
    }
}
