package com.jkojote.weblib.application.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.AuthorView;
import com.jkojote.weblib.application.views.BookView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("bookViewJsonConverter")
public class BookViewJsonConverter implements JsonConverter<BookView> {

    private JsonConverter<AuthorView> authorViewJsonConverter;

    public BookViewJsonConverter(@Qualifier("authorViewJsonConverter")
                                 JsonConverter<AuthorView> authorViewJsonConverter) {
        this.authorViewJsonConverter = authorViewJsonConverter;
    }

    @Override
    public JsonObject convertToJson(BookView view) {
        JsonObject json = new JsonObject();
        JsonArray authors = new JsonArray();
        for (AuthorView v : view.getAuthors())
            authors.add(authorViewJsonConverter.convertToJson(v));
        json.add("id", new JsonPrimitive(view.getInstanceId()));
        json.add("title", new JsonPrimitive(view.getFileUrl()));
        json.add("format", new JsonPrimitive(view.getFormat()));
        json.add("averageRating", new JsonPrimitive(view.getAverageRating()));
        json.add("url", new JsonPrimitive(view.getUrl()));
        json.add("imageUrl", new JsonPrimitive(view.getImageUrl()));
        json.add("fileUrl", new JsonPrimitive(view.getFileUrl()));
        json.add("authors", authors);
        return json;
    }
}
