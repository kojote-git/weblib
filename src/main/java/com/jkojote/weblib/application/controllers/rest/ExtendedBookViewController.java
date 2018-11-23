package com.jkojote.weblib.application.controllers.rest;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.extended.ExtendedBookView;
import com.jkojote.weblib.application.views.book.simple.BookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.errorResponse;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseJson;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/rest/extbooks")
public class ExtendedBookViewController {

    private JsonConverter<ExtendedBookView> jsonConverter;

    private ViewSelector<ExtendedBookView> viewSelector;

    private SqlClauseBuilder clauseBuilder;

    @Autowired
    public ExtendedBookViewController(
            @Qualifier("extendedBookViewJsonConverter")
            JsonConverter<ExtendedBookView> jsonConverter,
            @Qualifier("extendedBookViewSelector")
            ViewSelector<ExtendedBookView> viewSelector,
            SqlClauseBuilder clauseBuilder) {
        this.jsonConverter = jsonConverter;
        this.viewSelector = viewSelector;
        this.clauseBuilder = clauseBuilder;
    }

    @GetMapping("{id}")
    @CrossOrigin
    public ResponseEntity<String> getView(@PathVariable("id") long id) {
        SqlClause clause = clauseBuilder.where(BookView.ID).eq(id).build();
        List<ExtendedBookView> views = viewSelector.select(clause);
        if (views.size() == 0)
            return errorResponse("no such book with id " + id, NOT_FOUND);
        String res = jsonConverter.convertToString(views.get(0));
        return responseJson(res, OK);
    }
}
