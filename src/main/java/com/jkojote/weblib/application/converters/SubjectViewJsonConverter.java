package com.jkojote.weblib.application.converters;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.subject.SubjectView;
import org.springframework.stereotype.Component;

@Component("subjectViewJsonConverter")
class SubjectViewJsonConverter implements JsonConverter<SubjectView> {

    @Override
    public JsonObject convertToJson(SubjectView obj) {
        JsonObject json = new JsonObject();
        json.add("id", new JsonPrimitive(obj.getId()));
        json.add("value", new JsonPrimitive(obj.getSubject()));
        return json;
    }
}
