package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.account.User;
import project.service.campaign.Campaign;

import javax.servlet.http.HttpSession;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class CampaignController {

    @RequestMapping(value = "/myCampaigns", method = RequestMethod.GET)
    public String myCampaigns(Model model, HttpSession session) {
        User user = (User)session.getAttribute("userId");
        model.addAttribute("user", user);

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
    public String newCampaignPost(Model model, HttpSession session) {
		User user = (User)session.getAttribute("userId");
		model.addAttribute("user", user);

        return "campaigns";
    }


}
