package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by andrea on 23.11.2015.
 */
@Controller
public class CampaignController {

    @RequestMapping(value = "/myCampaigns", method = RequestMethod.GET)
    public String myCampaigns(Model model) {
        return "campaigns";
    }
    @RequestMapping(value = "/newCampaign", method = RequestMethod.GET)
    public String newCampaign(Model model) {
        return "newcampaign";
    }

}
