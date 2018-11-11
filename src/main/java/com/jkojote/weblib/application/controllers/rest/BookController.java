package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkojote.library.domain.model.book.instance.BookInstance;
import com.jkojote.library.domain.shared.domain.DomainRepository;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.ViewTranslator;
import com.jkojote.weblib.application.views.BookView;
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

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/rest/books")
public class BookController {

    private DomainRepository<BookInstance> bookInstanceRepository;

    private JsonConverter<BookView> bookViewJsonConverter;

    private ViewTranslator<BookInstance, BookView> bookViewTranslator;

    private final HttpHeaders defaultHeaders;

    @Autowired
    public BookController(
            @Qualifier("bookInstanceRepository")
            DomainRepository<BookInstance> bookInstanceRepository,
            @Qualifier("bookViewJsonConverter")
            JsonConverter<BookView> bookViewJsonConverter,
            @Qualifier("bookInstanceToBookViewTranslator")
            ViewTranslator<BookInstance, BookView> bookViewTranslator) {
        this.bookInstanceRepository = bookInstanceRepository;
        this.bookViewJsonConverter = bookViewJsonConverter;
        this.bookViewTranslator = bookViewTranslator;
        this.defaultHeaders =  new HttpHeaders();
        this.defaultHeaders.set("Content-Type", "application/json");
    }

    @GetMapping("")
    public ResponseEntity<String> getAll() {
        List<BookView> bookViews = bookViewTranslator
                .batchTranslate(bookInstanceRepository.findAll());
        JsonObject resp = new JsonObject();
        JsonArray array = new JsonArray();
        for (BookView view : bookViews)
            array.add(bookViewJsonConverter.convertToJson(view));
        resp.add("books", array);
        return new ResponseEntity<>(resp.toString(), defaultHeaders, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") long id) {
        BookInstance instance = bookInstanceRepository.findById(id);
        if (instance == null)
            return ControllerUtils.errorResponse("no bookInstance with id " + id, UNPROCESSABLE_ENTITY);
        JsonObject resp = bookViewJsonConverter
                .convertToJson(bookViewTranslator.translate(instance));
        return new ResponseEntity<>(resp.toString(), defaultHeaders, OK);
    }
}
