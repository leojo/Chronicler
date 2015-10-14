package dbLookup;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private static final String template = "Searching for a %s";

    @RequestMapping("/search")
    public String greeting(@RequestParam(value="s", defaultValue="") String searchString) {
        Lookup sr = new Lookup(searchString, "data/dnd.sqlite");
        return sr.getContent();
    }
}
