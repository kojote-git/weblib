package com.jkojote.weblib.application.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.extended.ExtendedBookView;
import com.jkojote.weblib.application.views.book.extended.FormatView;
import com.jkojote.weblib.application.views.book.simple.BookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("extendedBookViewJsonConverter")
class ExtendedBookViewJsonConverter implements JsonConverter<ExtendedBookView> {

    private JsonConverter<BookView> bookViewJsonConverter;

    private JsonConverter<FormatView> formatViewJsonConverter;

    @Autowired
    ExtendedBookViewJsonConverter(@Qualifier("bookViewJsonConverter")
                                  JsonConverter<BookView> bookViewJsonConverter,
                                  @Qualifier("formatViewJsonConverter")
                                  JsonConverter<FormatView> formatViewJsonConverter) {
        this.bookViewJsonConverter = bookViewJsonConverter;
        this.formatViewJsonConverter = formatViewJsonConverter;
    }


    @Override
    public JsonObject convertToJson(ExtendedBookView view) {
        JsonObject resultView = bookViewJsonConverter.convertToJson(view.getBase());
        JsonArray formats = new JsonArray();
        JsonArray subjects = new JsonArray();
        for (FormatView fv : view.getFormatViews()) {
            formats.add(formatViewJsonConverter.convertToJson(fv));
        }
        for (String subject : view.getSubjects()) {
            subjects.add(new JsonPrimitive(subject));
        }
        resultView.add("description", new JsonPrimitive(view.getDescriptionUrl()));
        resultView.add("formats", formats);
        resultView.add("subjects", subjects);
        return resultView;
    }
}
