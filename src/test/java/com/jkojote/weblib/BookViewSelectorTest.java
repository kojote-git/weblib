package com.jkojote.weblib;

import com.jkojote.library.clauses.SortOrder;
import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.views.book.BookView;
import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.jkojote.weblib.application.views.book.BookView.LANGUAGE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class BookViewSelectorTest {
    @Autowired
    private PageableViewSelector<BookView> bookViewSelector;

    @Autowired
    private JsonConverter<BookView> bookViewJsonConverter;

    @Autowired
    private SqlClauseBuilder sqlClauseBuilder;

    @Test
    public void selectAll() {
        SqlClause clause = sqlClauseBuilder
                .where("rating.average").lt(10)
                .build();
        SqlPageSpecification pageSpec = new SqlPageSpecificationImpl(clause, 3, 1);
        println(bookViewSelector.findAll(pageSpec));
        pageSpec = new SqlPageSpecificationImpl(clause, 3, 2);
        println(bookViewSelector.findAll(pageSpec));
        pageSpec = new SqlPageSpecificationImpl(clause, 3, 3);
        println(bookViewSelector.findAll(pageSpec));
        pageSpec = new SqlPageSpecificationImpl(clause, 3, 4);
        println(bookViewSelector.findAll(pageSpec));

    }

    private void println(List<BookView> bookViews) {
        System.out.println("____________________");
        for (BookView bookView : bookViews) {
            System.out.println(bookViewJsonConverter.convertToString(bookView));
        }
        System.out.println("____________________");
    }

}
