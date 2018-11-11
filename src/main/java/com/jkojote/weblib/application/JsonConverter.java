package com.jkojote.weblib.application;

import com.google.gson.JsonObject;


public interface JsonConverter<T> {

    JsonObject convertToJson(T obj);

    default String convertToString(T obj) {
        return convertToJson(obj).toString();
    }
}
