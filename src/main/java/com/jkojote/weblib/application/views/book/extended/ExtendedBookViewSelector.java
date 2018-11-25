package com.jkojote.weblib.application.views.book.extended;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.views.book.simple.BookView;
import com.jkojote.weblib.config.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.jkojote.weblib.application.views.book.extended.ExtendedBookView.ExtendedBookViewBuilder;

@Component("extendedBookViewSelector")
class ExtendedBookViewSelector implements ViewSelector<ExtendedBookView> {

    private static final String SELECT_SUBJECTS =
        "SELECT subject.subject FROM Subject subject " +
        "INNER JOIN WorkSubject ws ON ws.subjectId = subject.id " +
        "WHERE ws.workId = ?";

    private static final String SELECT_FORMAT_VIEWS =
        "SELECT bookInstance.id, bookInstance.format FROM BookInstance bookInstance " +
        "WHERE bookInstance.bookId = ?";

    private ViewSelector<BookView> bookViewSelector;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExtendedBookViewSelector(
            @Qualifier("bookViewSelector")
            ViewSelector<BookView> bookViewSelector,
            JdbcTemplate jdbcTemplate) {
        this.bookViewSelector = bookViewSelector;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ExtendedBookView> selectAll() {
        return convert(bookViewSelector.selectAll());
    }

    @Override
    public List<ExtendedBookView> select(Predicate<ExtendedBookView> predicate) {
        return selectAll().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<ExtendedBookView> select(SqlClause sqlClause) {
        return convert(bookViewSelector.select(sqlClause));
    }

    private List<ExtendedBookView> convert(List<BookView> views) {
        List<ExtendedBookView> res = new ArrayList<>();
        ExtendedBookViewBuilder builder = ExtendedBookViewBuilder.builder(true);
        for (BookView view : views) {
            ExtendedBookView t = builder
                    .withBase(view)
                    .withFormats(getFormatViews(view.getId()))
                    .withSubjects(getSubjects(view.getWorkId()))
                    .withDescriptionUrl(Shared.LISE + "rest/works/" + view.getWorkId() + "/description")
                    .build();
            res.add(t);
        }
        return res;
    }

    private List<String> getSubjects(long workId) {
        return jdbcTemplate.query(SELECT_SUBJECTS, (rs, rn) -> {
            return rs.getString("subject.subject");
        }, workId);
    }

    private List<FormatView> getFormatViews(long bookId) {
        return jdbcTemplate.query(SELECT_FORMAT_VIEWS, (rs, rn) -> {
           String url = Shared.LISE + "rest/instances/" + rs.getLong("bookInstance.id") + "/file";
           return new FormatView(rs.getString("bookInstance.format"), url, rs.getLong("bookInstance.id"));
        }, bookId);
    }
}
