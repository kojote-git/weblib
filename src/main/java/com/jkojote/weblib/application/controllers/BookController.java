package com.jkojote.weblib.application.controllers;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.views.book.extended.ExtendedBookView;
import com.jkojote.weblib.application.views.book.simple.BookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.getNotFound;

@Controller
@RequestMapping("books")
public class BookController {

    private ViewSelector<ExtendedBookView> viewSelector;

    private SqlClauseBuilder clauseBuilder;

    @Autowired
    public BookController(@Qualifier("extendedBookViewSelector")
                          ViewSelector<ExtendedBookView> viewSelector,
                          SqlClauseBuilder clauseBuilder) {
        this.clauseBuilder = clauseBuilder;
        this.viewSelector = viewSelector;
    }

    @GetMapping("{id}")
    public ModelAndView getBook(@PathVariable("id") long id) {
        SqlClause clause = clauseBuilder.where(BookView.ID).eq(id).build();
        List<ExtendedBookView> views = viewSelector.select(clause);
        if (views.size() == 0)
            return getNotFound();
        ExtendedBookView view = views.get(0);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("book", view);
        modelAndView.setViewName("book-view");
        return modelAndView;
    }

}
