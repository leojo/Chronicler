package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.campaign.Campaign;
import project.service.character.CharacterBean;
import project.service.dbLookup.AccountStorage;
import project.service.dbLookup.OfflineResultSet;

import javax.servlet.http.HttpSession;
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
	    OfflineResultSet campaigns = storage.getCampaigns(user.getUserID());
	    Vector<String> campaignNames = new Vector<>();
	    while (campaigns.next()) {
		    campaignNames.add(campaigns.getString("CampaignID"));
	    }

	    model.addAttribute("campaignList", campaignNames.toArray());

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

        return "campaigns";
    }


}
