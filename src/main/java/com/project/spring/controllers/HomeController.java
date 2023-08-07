package com.project.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    /*@GetMapping("/home")
    public String welcome() {
        return "home";
    }*/

    @GetMapping("/login")
    public String login(@RequestParam(name = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been successfully logged out.");
        }
        return "login";
    }

    /*@GetMapping("/user")
    public String getUser() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/403")
    public String authention() {
        return "403";
    }*/
}
