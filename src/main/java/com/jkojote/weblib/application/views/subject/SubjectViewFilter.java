package com.jkojote.weblib.application.views.subject;

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

@Component("subjectViewFilter")
class SubjectViewFilter implements ViewFilter<SubjectView> {

    private QueryStringParser parser;

    private PageableViewSelector<SubjectView> subjectSelector;

    @Autowired
    SubjectViewFilter(@Qualifier("subjectViewSelector")
                      PageableViewSelector<SubjectView> subjectSelector,
                      QueryStringParser parser) {
        this.parser = parser;
        this.subjectSelector = subjectSelector;
    }

    @Override
    public List<SubjectView> findAll(String queryString) {
        Map<String, String> params = parser.getParams(queryString);
        if (params.size() == 0)
            return subjectSelector.selectAll();
        if (params.containsKey("page")) {
            int page = parseInt(params.get("page"), "page must be valid integer");
            int pageSize;
            if (params.containsKey("pageSize"))
                pageSize = parseInt(params.get("pageSize"), "pageSize must be valid integer");
            else
                pageSize = 8;
            SqlPageSpecification pageSpecification =
                    new SqlPageSpecificationImpl(EmptySqlClause.getClause(), pageSize, page);
            return subjectSelector.findAll(pageSpecification);
        } else {
            return subjectSelector.selectAll();
        }
    }

    private int parseInt(String str, String message) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new MalformedQueryStringException(message);
        }
    }
}
