package com.jkojote.weblib.application.security;

public interface AuthorizationService {

    String authorize(String email, String password);

    void logout(String email);

    String getToken(String email);

    boolean checkToken(String email, String token);

    boolean authorized(String email);

}
