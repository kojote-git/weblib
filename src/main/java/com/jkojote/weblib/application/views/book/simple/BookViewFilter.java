package com.jkojote.weblib.application.views.book.simple;

import com.jkojote.library.clauses.*;
import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.utils.QueryStringParser;
import com.jkojote.weblib.application.utils.EmptySqlClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import com.jkojote.weblib.application.utils.ViewFilter;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("bookViewFilter")
class BookViewFilter implements ViewFilter<BookView> {

    private QueryStringParser parser;

    private PageableViewSelector<BookView> bookViewSelector;

    private SqlClauseBuilder sqlClauseBuilder;


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BookViewFilter(QueryStringParser parser,
                          @Qualifier("bookViewSelector")
                          PageableViewSelector<BookView> bookViewSelector,
                          SqlClauseBuilder sqlClauseBuilder,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.parser = parser;
        this.bookViewSelector = bookViewSelector;
        this.sqlClauseBuilder = sqlClauseBuilder;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<BookView> findAll(String queryString) {
        Map<String, String> params = parser.getParams(queryString);
        if (params.size() == 0)
            return bookViewSelector.selectAll();
        List<BookView> views;
        SqlConditionChain conditionChain = getConditionChain(params);
        SqlClause sqlClause = getClause(params, conditionChain);
        if (!params.containsKey("author") && !params.containsKey("subject") &&
            !params.containsKey("title")) {
            SqlPageSpecification pageSpecification = getPageSpecification(params, sqlClause);
            if (pageSpecification == null) {
                views = bookViewSelector.select(sqlClause);
                return views;
            }
            else
                return bookViewSelector.findAll(pageSpecification);
        }
        views = bookViewSelector.select(sqlClause);
        Predicate<BookView> predicate = b -> true;
        if (params.containsKey("author")) {
            String[] authorIds = params.get("author").split(",");
            Set<Long> authors = new TreeSet<>();
            for (String author : authorIds) {
                authors.add(parseLong(author, "author's id must be valid integer"));
            }
            predicate = predicate.and(view -> view.getAuthors().stream()
                            .anyMatch(authorView -> authors.contains(authorView.getId())));
        }
        if (params.containsKey("subject")) {
            predicate = predicate.and(getPredicateForSubjects(params));
        }
        if (params.containsKey("title")) {
            String title = params.get("title");
            Pattern pattern = Pattern.compile(title,
                Pattern.UNICODE_CASE |
                Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
            predicate = predicate.and(view -> pattern.matcher(view.getTitle()).find());
        }
        views = views.stream().filter(predicate).collect(Collectors.toList());
        if (params.containsKey("page")) {
            SqlPageSpecification pageSpecification = getPageSpecification(params, EmptySqlClause.getClause());
            int page = pageSpecification.page();
            int pageSize = pageSpecification.pageSize();
            int offset = (page - 1) * pageSize;
            if (offset > views.size())
                return Collections.emptyList();
            if (offset + pageSize > views.size())
                pageSize = views.size() - offset;
            return views.subList(offset, offset + pageSize);
        }
        return views;
    }

    private Predicate<BookView> getPredicateForSubjects(Map<String, String> params) {
        String[] subjects = params.get("subject").split(",");
        Set<String> subjectsSet = new TreeSet<>();
        Collections.addAll(subjectsSet, subjects);
        String SELECT =
            "SELECT DISTINCT book.id AS book_id FROM Book book " +
                "INNER JOIN WorkSubject subjects ON book.workId = subjects.workId "+
                "INNER JOIN Subject subject ON subject.id = subjects.subjectId " +
            "WHERE subject.subject IN (:subjects)";
        List<Long> ids = namedParameterJdbcTemplate.query(SELECT,
                Collections.singletonMap("subjects", subjectsSet),
                (rs, rn) -> rs.getLong("book_id"));
        return (BookView bookView) -> ids.contains(bookView.getId());
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

    private long parseLong(String str, String message) {
        try {
            return Long.parseLong(str);
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
