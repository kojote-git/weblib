package com.jkojote.weblib.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LibraryController {

    @GetMapping("")
    public String getMain() {
        return "main-page";
    }
}
