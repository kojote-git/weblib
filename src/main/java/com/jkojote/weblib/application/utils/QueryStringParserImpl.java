package com.jkojote.weblib.application.utils;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component("queryStringParser")
class QueryStringParserImpl implements QueryStringParser {


    @Override
    public Map<String, String> getParams(String queryString) {
        if (queryString == null)
            return Collections.emptyMap();
        Map<String, String> pairs = new HashMap<>();
        String[] params = queryString.split("&");
        for (String param : params) {
            int idx = param.indexOf("=");
            if (idx == -1)
                continue;
            String paramName = param.substring(0, idx);
            String paramValue = param.substring(idx + 1).replaceAll("%20", " ");
            if (!pairs.containsKey(paramName))
                pairs.put(paramName, paramValue);
        }
        return pairs;
    }
}
