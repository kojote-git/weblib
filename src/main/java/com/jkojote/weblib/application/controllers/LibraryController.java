package com.jkojote.weblib.application.controllers;

import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.security.AuthorizationService;
import com.jkojote.weblib.config.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static com.jkojote.weblib.application.utils.ControllerUtils.readCookie;

@Controller
@RequestMapping("/")
public class LibraryController {

    private AuthorizationService authorizationService;

    @Autowired
    public LibraryController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("")
    public ModelAndView getMain(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<String> email = readCookie("email", req);
        Optional<String> token = readCookie("accessToken", req);
        if (email.isPresent() && token.isPresent()) {
            if (authorizationService.checkToken(email.get(), token.get())) {
                modelAndView.addObject("email", email.get());
            }
        }
        modelAndView.setViewName("main-page");
        modelAndView.addObject("authorizationUrl", Shared.HOST + "authorization");
        modelAndView.addObject("registrationUrl", Shared.HOST + "registration");
        return modelAndView;
    }

    @GetMapping("authorization")
    public ModelAndView getAuthorization() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("authorization");
        return modelAndView;
    }

    @GetMapping("registration")
    public ModelAndView getRegistration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }
}
