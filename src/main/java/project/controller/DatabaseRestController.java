package project.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
@RequestMapping("getString")
public class DatabaseRestController {

    @RequestMapping(value = "/{thisString}", method = RequestMethod.GET, produces = "application/json")
    public String StringResponse(@PathVariable String thisString) {
        return thisString;
    }

    /*
    public class response{
        private final String s;

        public response(String string){
            s = string;
        }
    }*/
}
