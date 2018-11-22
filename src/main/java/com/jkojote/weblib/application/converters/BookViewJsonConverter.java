package com.jkojote.weblib.application.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.author.AuthorView;
import com.jkojote.weblib.application.views.book.simple.BookView;
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
        json.add("id", new JsonPrimitive(view.getId()));
        json.add("title", new JsonPrimitive(view.getTitle()));
        json.add("averageRating", new JsonPrimitive(view.getAverageRating()));
        json.add("lang", new JsonPrimitive(view.getLanguageCode().toString()));
        json.add("url", new JsonPrimitive(view.getUrl()));
        json.add("imageUrl", new JsonPrimitive(view.getImageUrl()));
        json.add("authors", authors);
        return json;
    }
}
