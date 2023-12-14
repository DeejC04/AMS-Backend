package com.ams.restapi.googleOAuth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleOAuthController {
    
    @GetMapping("/")
    public String home() {
        return "Hello World";
    } 

    @GetMapping("/secure")
    public String secured(){
        return "index";
    }
}
