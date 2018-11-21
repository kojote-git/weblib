package com.jkojote.weblib.application.views.book;

import com.jkojote.library.clauses.*;
import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.utils.QueryStringParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.jkojote.weblib.application.utils.ViewFilter;

import java.util.List;
import java.util.Map;

@Component("bookViewFilter")
class BookViewFilter implements ViewFilter<BookView> {

    private QueryStringParser parser;

    private PageableViewSelector<BookView> bookViewSelector;

    private SqlClauseBuilder sqlClauseBuilder;

    @Autowired
    public BookViewFilter(QueryStringParser parser,
                          @Qualifier("bookViewSelector")
                          PageableViewSelector<BookView> bookViewSelector,
                          SqlClauseBuilder sqlClauseBuilder) {
        this.parser = parser;
        this.bookViewSelector = bookViewSelector;
        this.sqlClauseBuilder = sqlClauseBuilder;
    }

    @Override
    public List<BookView> findAll(String queryString) {
        Map<String, String> params = parser.getParams(queryString);
        if (params.size() == 0)
            return bookViewSelector.selectAll();
        List<BookView> views;
        SqlConditionChain conditionChain = getConditionChain(params);
        SqlClause sqlClause = getClause(params, conditionChain);
        SqlPageSpecification pageSpecification = getPageSpecification(params, sqlClause);
        if (pageSpecification == null)
            views = bookViewSelector.select(sqlClause);
        else
            views = bookViewSelector.findAll(pageSpecification);
        return views;
    }

    private SqlPageSpecification getPageSpecification(Map<String, String> params, SqlClause clause) {
        if (params.containsKey("page")) {
            int page = parseInt(params.get("page"), "page must be valid integer");
            int pageSize = 8;
            if (params.containsKey("pageSize"))
                pageSize = parseInt(params.get("pageSize"), "pageSize must be valid integer");
            try {
                return new SqlPageSpecificationImpl(clause, pageSize, page);
            } catch (IllegalArgumentException e) {
                throw new MalformedQueryStringException(e.getMessage());
            }
        }
        return null;
    }

    private SqlConditionChain getConditionChain(Map<String, String> params) {
        SqlConditionChain conditionChain = null;
        if (params.containsKey("lang")) {
            String[] langs = params.get("lang").split(",");
            for (String lang : langs) {
                if (conditionChain == null)
                    conditionChain = sqlClauseBuilder.where(BookView.LANGUAGE).like(lang);
                else
                    conditionChain.or(BookView.LANGUAGE).like(lang);
            }
        }
        return conditionChain;
    }

    private int parseInt(String str, String message) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new MalformedQueryStringException(message);
        }
    }

    private SqlClause getClause(Map<String, String> params, SqlConditionChain conditionChain) {
        if (params.containsKey("order")) {
            String[] order = params.get("order").split(",");
            if (order.length < 2)
                throw new MalformedQueryStringException("inappropriate parameters for ordering");
            String orderBy;
            SortOrder sortOrder;
            switch (order[0]) {
                case "rating" : orderBy = BookView.AVG_RATING; break;
                case "title" : orderBy = BookView.TITLE; break;
                default: throw new MalformedQueryStringException("inappropriate parameter for ordering");
            }
            try {
                sortOrder = SortOrder.valueOf(order[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new MalformedQueryStringException("inappropriate sort order");
            }
            return conditionChain != null ? conditionChain.orderBy(orderBy, sortOrder).build() :
                    sqlClauseBuilder.orderBy(orderBy, sortOrder).build();
        }
        return conditionChain != null ? conditionChain.build() : EmptySqlClause.getClause();
    }
}
