package com.jkojote.weblib.application.utils;

import java.util.Map;

public interface QueryStringParser {

    Map<String, String> getParams(String queryString);

}
