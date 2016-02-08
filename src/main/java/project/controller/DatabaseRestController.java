package project.controller;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by andrea on 8.2.2016.
 */
@RestController
public class DatabaseRestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


    public class Greeting {
        private final long id;
        private final String content;
        public Greeting(long id, String content) {
            this.id = id;
            this.content = content;
        }
        public long getId() { return id; }
        public String getContent() { return content; }
    }
}
