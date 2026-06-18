package com.ams.core.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Redirect any unknown pages or 404 errors back to the main index
        // The index.html handles routing the user to the correct dashboard or login page
        return "redirect:/index.html";
    }
}
