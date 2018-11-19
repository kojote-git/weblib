package com.jkojote.weblib.application.converters;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.author.AuthorView;
import org.springframework.stereotype.Component;

@Component("authorViewJsonConverter")
public class AuthorViewJsonConverter implements JsonConverter<AuthorView> {

    @Override
    public JsonObject convertToJson(AuthorView view) {
        JsonObject json = new JsonObject();
        json.add("id", new JsonPrimitive(view.getId()));
        json.add("firstName", new JsonPrimitive(view.getFirstName()));
        json.add("middleName", new JsonPrimitive(view.getMiddleName()));
        json.add("lastName", new JsonPrimitive(view.getLastName()));
        return json;
    }
}
