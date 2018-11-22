package com.jkojote.weblib.application.views.author;

import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.weblib.application.utils.EmptySqlClause;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.utils.QueryStringParser;
import com.jkojote.weblib.application.utils.ViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("authorViewFilter")
class AuthorViewFilter implements ViewFilter<AuthorView> {

    private PageableViewSelector<AuthorView> authorViewSelector;

    private QueryStringParser parser;

    @Autowired
    AuthorViewFilter(@Qualifier("authorViewSelector")
                     PageableViewSelector<AuthorView> authorViewSelector,
                     QueryStringParser parser) {
        this.authorViewSelector = authorViewSelector;
        this.parser = parser;
    }

    @Override
    public List<AuthorView> findAll(String queryString) {
        Map<String, String> params = parser.getParams(queryString);
        if (params.size() == 0)
            return authorViewSelector.selectAll();
        if (params.containsKey("page")) {
            int page = parseInt(params.get("page"), "page must be valid integer");
            int pageSize = 8;
            if (params.containsKey("pageSize")) {
                pageSize = parseInt(params.get("pageSize"), "pageSize must be valid integer");
            }
            SqlPageSpecification pageSpecification = new SqlPageSpecificationImpl(EmptySqlClause.getClause(), pageSize, page);
            return authorViewSelector.findAll(pageSpecification);
        }
        return Collections.emptyList();
    }

    private int parseInt(String str, String message) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new MalformedQueryStringException(message);
        }
    }
}
