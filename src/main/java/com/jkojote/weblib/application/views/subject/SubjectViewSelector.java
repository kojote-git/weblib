package com.jkojote.weblib.application.views.subject;

import com.jkojote.library.clauses.SqlClause;
import com.jkojote.library.domain.shared.SqlPageSpecificationImpl;
import com.jkojote.library.domain.shared.domain.PageableViewSelector;
import com.jkojote.library.domain.shared.domain.SqlPageSpecification;
import com.jkojote.weblib.application.utils.EmptySqlClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("subjectViewSelector")
class SubjectViewSelector implements PageableViewSelector<SubjectView> {

    private static final String SELECT =
        "SELECT subject.id, subject.subject FROM subject ";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    SubjectViewSelector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SubjectView> selectAll() {
        return jdbcTemplate.query(SELECT, this::map);
    }

    @Override
    public List<SubjectView> select(Predicate<SubjectView> predicate) {
        return selectAll().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<SubjectView> select(SqlClause sqlClause) {
        return jdbcTemplate.query(SELECT + sqlClause.asString(), this::map);
    }

    @Override
    public List<SubjectView> findAll(int page, int pageSize) {
        return findAll(new SqlPageSpecificationImpl(EmptySqlClause.getClause(), pageSize, page));
    }

    @Override
    public List<SubjectView> findAll(SqlPageSpecification sqlPageSpecification) {
        int page = sqlPageSpecification.page();
        int pageSize = sqlPageSpecification.pageSize();
        int offset = (page - 1) * pageSize;
        return jdbcTemplate.query(SELECT + sqlPageSpecification.predicate().asString() + " LIMIT ? OFFSET ?",
                this::map, pageSize, offset);
    }

    @Override
    public List<SubjectView> findAll(int page, int pageSize, Predicate<SubjectView> predicate) {
        return findAll(page, pageSize).stream().filter(predicate).collect(Collectors.toList());
    }

    private SubjectView map(ResultSet rowSet, int rowNum) throws SQLException {
        return new SubjectView(rowSet.getInt("subject.id"), rowSet.getString("subject.subject"));
    }
}
