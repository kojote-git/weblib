package com.jkojote.weblib.application.views.author;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.utils.EmptySqlClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("authorViewSelector")
class AuthorViewSelector implements PageableViewSelector<AuthorView> {

    private static final String SELECT =
        "SELECT " +
            "author.id, author.firstName, author.middleName, author.lastName " +
        "FROM Author author";

    private JdbcTemplate jdbcTemplate;

    private RowMapper<AuthorView> authorViewMapper;

    @Autowired
    public AuthorViewSelector(JdbcTemplate jdbcTemplate,
                              @Qualifier("authorViewMapper")
                              RowMapper<AuthorView> authorViewMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorViewMapper = authorViewMapper;
    }

    @Override
    public List<AuthorView> selectAll() {
        return jdbcTemplate.query(SELECT, authorViewMapper);
    }

    @Override
    public List<AuthorView> select(Predicate<AuthorView> predicate) {
        return jdbcTemplate.query(SELECT, authorViewMapper).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorView> select(SqlClause sqlClause) {
        return jdbcTemplate.query(SELECT + sqlClause.asString(), authorViewMapper);
    }

    @Override
    public List<AuthorView> findAll(int page, int pageSize) {
        return findAll(new SqlPageSpecificationImpl(EmptySqlClause.getClause(), pageSize, page));
    }

    @Override
    public List<AuthorView> findAll(SqlPageSpecification sqlPageSpecification) {
        int page = sqlPageSpecification.page();
        int pageSize = sqlPageSpecification.pageSize();
        int offset = (page - 1) * pageSize;
        SqlClause clause = sqlPageSpecification.predicate();
        return jdbcTemplate.query(SELECT + clause.asString() + " LIMIT ? OFFSET ?", authorViewMapper,
                pageSize, offset);
    }

    @Override
    public List<AuthorView> findAll(int page, int pageSize, Predicate<AuthorView> predicate) {
        return findAll(page, pageSize).stream().filter(predicate).collect(Collectors.toList());
    }
}
