package com.jkojote.weblib.application.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

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
}
