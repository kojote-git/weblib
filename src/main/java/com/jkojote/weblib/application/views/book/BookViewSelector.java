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

    private static final String SELECT_ALL;

    static {
        SELECT_ALL = new StringBuilder()
                .append("SELECT ")
                  .append("book.title AS book_title, ")
                  .append("book.id AS book_id, ")
                  .append("book.lang AS book_lang, ")
                  .append("book.workId AS work_id, ")
                  .append("rating.average AS rating_average, ")
                  .append("bookInstance.id AS book_instance_id ")
                .append("FROM Book AS book ")
                .append("LEFT JOIN ( ")
                  .append("SELECT MIN(bi1.id) AS id, bi1.bookId ")
                  .append("FROM BookInstance bi1 ")
                  .append("LEFT JOIN (")
                    .append("SELECT id FROM BookInstance WHERE OCTET_LENGTH(cover) > 0")
                  .append(") AS bi2 ")
                    .append("ON bi1.id = bi2.id ")
                    .append("GROUP BY bi1.bookId")
                .append(") AS bookInstance ")
                  .append("ON bookInstance.bookId = book.id ")
                .append("LEFT JOIN (")
                  .append("SELECT bookId, AVG(rating) AS average FROM Rating GROUP BY bookId")
                .append(") AS rating ")
                  .append("ON rating.bookId = book.id")
                .toString();
    }


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
