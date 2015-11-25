package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.campaign.Campaign;
import project.service.character.CharacterBean;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.OfflineResultSet;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class CampaignController {
	AccountStorage storage;

    @RequestMapping(value = "/myCampaigns", method = RequestMethod.GET)
    public String myCampaigns(Model model, HttpSession session) {
	    storage = new AccountStorage("data/userAccounts.sqlite");
        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);
		System.err.println("Getting campaigns...");
		/*
		HashMap<Integer, String> campaigns = storage.getCampaigns(user.getUserID());
		System.err.println("done!");
		if (campaigns == null) {
		    return "campaigns";
	    }
	    Vector<String> campaignNames = new Vector<>();
	    while (campaigns.next()) {
		    campaignNames.add(campaigns.getString("CampaignID"));
	    }*/

	    model.addAttribute("campaignList", storage.getCampaigns(user.getUserID()));

        return "campaigns";
    }

    @RequestMapping(value = "/newCampaign", method = RequestMethod.GET)
    public String newCampaign(Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

	    Campaign campaign = new Campaign();
	    model.addAttribute("campaign", campaign);

        return "newcampaign";
    }

	@RequestMapping(value = "/newCampaign", method = RequestMethod.POST)
    public String newCampaignPost(@ModelAttribute Campaign campaign, Model model, HttpSession session) {
		User user = (User)session.getAttribute("userId");
		model.addAttribute("user", user);

		storage = new AccountStorage("data/userAccounts.sqlite");

		model.addAttribute("campaign", campaign);
		System.out.println("Campaign name: " + campaign.getName());
		storage.insertCampaign(user.getUserID(), campaign.getName());

        return "redirect:myCampaigns";
    }

	@RequestMapping(value = "deleteCampaign{charID}", method = RequestMethod.GET)
	public String deleteCharacter(@PathVariable int charID, Model model, HttpSession session) {
		User user = (User)session.getAttribute("userId");
		model.addAttribute("user", user);

		System.out.println("WE PRESSED THAT BUTTON BOOM");
		System.out.println("WERE DELETING CAMPAIGN WITH ID "+charID);
		storage = new AccountStorage("data/userAccounts.sqlite");
		storage.deleteCampaign(charID);

		return "myCharacters";
	}

}
