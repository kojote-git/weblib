package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.BookView;
import com.jkojote.weblib.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/rest/books")
public class BookController {

    private JsonConverter<BookView> bookViewJsonConverter;

    private ViewSelector<BookView> bookViewSelector;

    private final HttpHeaders defaultHeaders;

    @Autowired
    public BookController(
            @Qualifier("bookViewJsonConverter")
            JsonConverter<BookView> bookViewJsonConverter,
            @Qualifier("bookViewSelector")
            ViewSelector<BookView> bookViewSelector) {
        this.bookViewJsonConverter = bookViewJsonConverter;
        this.bookViewSelector = bookViewSelector;
        this.defaultHeaders =  new HttpHeaders();
        this.defaultHeaders.set("Content-Type", "application/json");
    }

    @GetMapping("")
    public ResponseEntity<String> getAll() {
        List<BookView> bookViews = bookViewSelector.selectAll();
        JsonObject resp = new JsonObject();
        JsonArray array = new JsonArray();
        for (BookView view : bookViews)
            array.add(bookViewJsonConverter.convertToJson(view));
        resp.add("books", array);
        return new ResponseEntity<>(resp.toString(), defaultHeaders, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") long id) {
        List<BookView> bookViews = bookViewSelector.select(view -> view.getId() == id);
        if (bookViews.size() == 0)
            return ControllerUtils.errorResponse("no bookInstance with id " + id, NOT_FOUND);
        String response = bookViewJsonConverter.convertToString(bookViews.get(0));
        return new ResponseEntity<>(response, OK);
    }
}
