package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.views.book.BookView;
import com.jkojote.weblib.application.utils.ViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.errorResponse;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseJson;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/rest/books")
public class BookController {

    private JsonConverter<BookView> bookViewJsonConverter;

    private ViewSelector<BookView> bookViewSelector;

    private ViewFilter<BookView> bookViewFilter;

    @Autowired
    public BookController(
            @Qualifier("bookViewJsonConverter")
            JsonConverter<BookView> bookViewJsonConverter,
            @Qualifier("bookViewFilter")
            ViewFilter<BookView> bookViewFilter,
            @Qualifier("bookViewSelector")
            ViewSelector<BookView> bookViewSelector) {
        this.bookViewJsonConverter = bookViewJsonConverter;
        this.bookViewFilter = bookViewFilter;
        this.bookViewSelector = bookViewSelector;
    }

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<String> getAll(HttpServletRequest req) {
        try {
            String queryString = req.getQueryString();
            List<BookView> bookViews = bookViewFilter.findAll(queryString);
            JsonObject resp = new JsonObject();
            JsonArray array = new JsonArray();
            for (BookView view : bookViews)
                array.add(bookViewJsonConverter.convertToJson(view));
            resp.add("books", array);
            return responseJson(resp.toString(), OK);
        } catch (MalformedQueryStringException e) {
            return errorResponse(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<String> getById(@PathVariable("id") long id) {
        List<BookView> bookViews = bookViewSelector.select(view -> view.getId() == id);
        if (bookViews.size() == 0)
            return errorResponse("no bookInstance with id " + id, NOT_FOUND);
        String response = bookViewJsonConverter.convertToString(bookViews.get(0));
        return responseJson(response, OK);
    }
}
