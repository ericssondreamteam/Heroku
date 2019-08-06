package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String getHome(){return "home";}

    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/mail")
    public String getMail(){
        return "mail";
    }

}