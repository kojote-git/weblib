package com.jkojote.weblib.application.security;

import java.util.Random;

public class Utils {

    private Utils() { throw new AssertionError(); }

    private static final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final Random random = new Random(9999999999L);

    public static String randomAlphaNumeric(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int j = Math.abs(random.nextInt()) % ALPHA_NUMERIC.length();
            sb.append(ALPHA_NUMERIC.charAt(j));
        }
        return sb.toString();
    }
}
