package com.jkojote.weblib.application.views.author;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component("authorViewMapper")
public class AuthorViewMapper implements RowMapper<AuthorView> {

    @Override
    public AuthorView mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("author.id");
        String
            firstName = rs.getString("author.firstName"),
            middleName = rs.getString("author.middleName"),
            lastName = rs.getString("author.lastName");
        return AuthorView.AuthorViewBuilder.anAuthorView()
                .withId(id)
                .withFirstName(firstName)
                .withMiddleName(middleName)
                .withLastName(lastName)
                .build();
    }
}
