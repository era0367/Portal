package knu.myhealthhub.portalnew.controller.individual;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String login() {
        return "individual/login";
    }
}
