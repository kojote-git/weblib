package com.jkojote.weblib.application.views.book;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("bookViewSelector")
public class BookViewSelector implements PageableViewSelector<BookView> {

    private static final String SELECT_ALL =
        "SELECT " +
          "work.id, work.title, bookInstance.format, bookInstance.id, ratings.averageRating " +
        "FROM BookInstance AS bookInstance " +
          "INNER JOIN Book AS book " +
            "ON bookInstance.bookId = book.id " +
          "INNER JOIN Work AS work " +
            "ON book.workId = work.id " +
          "LEFT JOIN ( " +
            "SELECT " +
              "bookInstanceId, AVG(readerRating) AS averageRating " +
            "FROM Download GROUP BY bookInstanceId " +
          ") AS ratings " +
            "ON ratings.bookInstanceId = bookInstance.id ";


    private JdbcTemplate jdbcTemplate;

    private RowMapper<BookView> bookViewMapper;

    @Autowired
    public BookViewSelector(JdbcTemplate jdbcTemplate,
                            @Qualifier("bookViewMapper")
                            RowMapper<BookView> bookViewMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookViewMapper = bookViewMapper;
    }

    @Override
    public List<BookView> selectAll() {
        return jdbcTemplate.query(SELECT_ALL, bookViewMapper);
    }

    @Override
    public List<BookView> select(Predicate<BookView> predicate) {
        return selectAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookView> select(SqlClause sqlClause) {
        return jdbcTemplate.query(SELECT_ALL + sqlClause.asString(), bookViewMapper);
    }

    @Override
    public List<BookView> findAll(int page, int pageSize) {
        return null;
    }

    @Override
    public List<BookView> findAll(SqlPageSpecification sqlPageSpecification) {
        int page = sqlPageSpecification.page();
        int size = sqlPageSpecification.pageSize();
        int offset = (page - 1) * size;
        SqlClause clause = sqlPageSpecification.predicate();
        String query = new StringBuilder(SELECT_ALL)
                .append(clause.asString()).append(" OFFSET ? LIMIT ?")
                .toString();
        return jdbcTemplate.query(query, bookViewMapper, offset, size);
    }

    @Override
    public List<BookView> findAll(int page, int pageSize, Predicate<BookView> predicate) {
        return findAll(page, pageSize).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
