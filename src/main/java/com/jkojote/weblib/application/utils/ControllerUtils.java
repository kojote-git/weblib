package com.jkojote.weblib.application.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.Shared;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ControllerUtils {

    private ControllerUtils() { throw new AssertionError(); }

    private static HttpHeaders jsonUtf8Headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    public static ModelAndView getNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("not-found");
        return modelAndView;
    }

    public static Optional<String> readCookie(String name, HttpServletRequest request) {
        if (request.getCookies() == null)
            return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findAny();
    }

    public static ResponseEntity<String> errorResponse(String message, HttpStatus status) {
        JsonObject json = new JsonObject();
        json.add("error", new JsonPrimitive(message));
        return new ResponseEntity<>(json.toString(), jsonUtf8Headers(), status);
    }

    public static ResponseEntity<String> responseMessage(String message, HttpStatus status) {
        JsonObject json = new JsonObject();
        json.add("message", new JsonPrimitive(message));
        return new ResponseEntity<>(json.toString(), jsonUtf8Headers(), status);
    }

    public static ResponseEntity<String> responseJson(String json, HttpStatus status) {
        return new ResponseEntity<>(json, jsonUtf8Headers(), status);
    }

    public static Map<String, String> getHeaderHrefs() {
        Map<String, String> res = new HashMap<>();
        res.put("weblibUrl", Shared.HOST);
        res.put("authorizationUrl", Shared.HOST + "authorization");
        res.put("registrationUrl", Shared.HOST + "registration");
        return res;
    }
}
