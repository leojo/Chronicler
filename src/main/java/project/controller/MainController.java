package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.character.CharacterSheet;
import project.service.globals.AbilityID;
import project.service.globals.SavingThrowID;

import java.sql.ResultSet;

import static project.service.globals.AbilityID.STR;


/**
 * Created by andrea on 19.10.2015.
 */
@Controller
public class MainController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "index";
    }


    @RequestMapping(value = "/dummySheet", method = RequestMethod.GET)
    public String characterSheet(Model sheetModel) {
        CharacterSheet c = new CharacterSheet();
        c.abilityScores.get(AbilityID.WIS).bonuses.put("Base Score", 14);
        c.levelUp(1);
        c.levelUp(2);
        c.levelUp(2);
        c.update();

        sheetModel.addAttribute("classID", c.classID);
        sheetModel.addAttribute("raceID", c.raceID);
        sheetModel.addAttribute("hitPoints", c.currentHP);
        sheetModel.addAttribute("BAB", c.BAB);
        sheetModel.addAttribute("saveFort", c.savingThrows.get(SavingThrowID.FORT));
        sheetModel.addAttribute("saveWill", c.savingThrows.get(SavingThrowID.WILL));
        sheetModel.addAttribute("saveReflex", c.savingThrows.get(SavingThrowID.REF));
        sheetModel.addAttribute("level", c.level);
        sheetModel.addAttribute("name", c.name);

        return "index";
    }
}


