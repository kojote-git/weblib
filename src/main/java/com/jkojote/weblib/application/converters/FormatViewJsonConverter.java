package com.jkojote.weblib.application.converters;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.extended.FormatView;
import org.springframework.stereotype.Component;

@Component("formatViewJsonConverter")
public class FormatViewJsonConverter implements JsonConverter<FormatView> {


    @Override
    public JsonObject convertToJson(FormatView view) {
        JsonObject json = new JsonObject();
        json.add("instanceId", new JsonPrimitive(view.getInstanceId()));
        json.add("value", new JsonPrimitive(view.getFormat()));
        json.add("url", new JsonPrimitive(view.getUrl()));
        return json;
    }
}
