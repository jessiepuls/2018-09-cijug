package hello;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/api/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/api/slow-greeting")
    public Greeting slowGreeting(@RequestParam(value="name", defaultValue="World") String name) throws InterruptedException {
        Random r = new Random();

        Thread.sleep(r.nextInt(500));
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}