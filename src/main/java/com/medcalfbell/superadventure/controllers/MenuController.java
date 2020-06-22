package com.medcalfbell.superadventure.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    @GetMapping
    public String getMenu() {
        /*
         * The String you're returning here is the 'view' (the page you want to load).
         * It corresponds to a file of the same name in src/main/resources/templates/menu.html
         */
        return "game";
    }
}
