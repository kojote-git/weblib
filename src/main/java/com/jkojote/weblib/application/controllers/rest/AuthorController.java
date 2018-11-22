package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.utils.ViewFilter;
import com.jkojote.weblib.application.views.author.AuthorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.errorResponse;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseJson;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/rest/authors")
public class AuthorController {

    private ViewFilter<AuthorView> authorViewFilter;

    private JsonConverter<AuthorView> authorViewJsonConverter;

    @Autowired
    public AuthorController(@Qualifier("authorViewFilter")
                            ViewFilter<AuthorView> authorViewFilter,
                            @Qualifier("authorViewJsonConverter")
                            JsonConverter<AuthorView> authorViewJsonConverter) {
        this.authorViewFilter = authorViewFilter;
        this.authorViewJsonConverter = authorViewJsonConverter;
    }

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<String> getAll(HttpServletRequest req) {
        try {
            String queryString = req.getQueryString();
            List<AuthorView> views = authorViewFilter.findAll(queryString);
            JsonObject json = new JsonObject();
            JsonArray authors = new JsonArray();
            json.add("authors", authors);
            for (AuthorView authorView : views) {
                authors.add(authorViewJsonConverter.convertToJson(authorView));
            }
            return responseJson(json.toString(), OK);
        } catch (MalformedQueryStringException e) {
            return errorResponse(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }
}
