package com.jkojote.weblib.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ControllerUtils {

    private ControllerUtils() { throw new AssertionError(); }

    public static ResponseEntity<String> errorResponse(String message, HttpStatus status) {
        JsonObject jsonResp = new JsonObject();
        jsonResp.add("message", new JsonPrimitive(message));
        return new ResponseEntity<>(jsonResp.toString(), status);
    }
}
