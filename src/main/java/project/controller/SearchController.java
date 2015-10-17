package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.dbLookup.Lookup;

import java.sql.*;

@RestController
public class SearchController {

    private static final String template = "Searching for a %s";

    @RequestMapping("/search")
    public ResultSet greeting(@RequestParam(value="s", defaultValue="") String searchString) {
        Lookup sr = new Lookup("data/dnd.sqlite");
        return sr.skill(searchString);
    }
}
