package com.jkojote.weblib;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.extended.ExtendedBookView;
import com.jkojote.weblib.application.views.book.simple.BookView;
import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class ExtendedBookViewSelectorTest {

    @Autowired
    private ViewSelector<ExtendedBookView> viewSelector;

    @Autowired
    private JsonConverter<ExtendedBookView> jsonConverter;

    @Autowired
    private SqlClauseBuilder clauseBuilder;

    @Test
    public void selectAll() {
        printAll(viewSelector.selectAll());
        SqlClause clause = clauseBuilder.where(BookView.ID).eq(3).build();
        System.out.println();
        printAll(viewSelector.select(clause));
    }

    private void printAll(List<ExtendedBookView> bookViews) {
        for (ExtendedBookView view : bookViews) {
            System.out.println(jsonConverter.convertToString(view));
        }
    }
}
