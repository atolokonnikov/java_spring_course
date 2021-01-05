package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.RegisterService;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    private final Logger logger = Logger.getLogger(RegisterController.class);
    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping(value = "/register")
    public String users(Model model) {
        logger.info("GET /register returns register_page.html");
        model.addAttribute("user", new User("", ""));
        return "register_page";
    }

    @PostMapping(value = "add_user")
    public String addUser(User user) {
        registerService.add_user(user);
        logger.info("Add user");
        return "redirect:/login";
    }
}
