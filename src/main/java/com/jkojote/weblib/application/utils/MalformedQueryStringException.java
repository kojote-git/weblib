package com.jkojote.weblib.application.utils;

public class MalformedQueryStringException extends RuntimeException {

    public MalformedQueryStringException(String message) {
        super(message);
    }

    public MalformedQueryStringException() {
        super("query string is malformed");
    }
}
