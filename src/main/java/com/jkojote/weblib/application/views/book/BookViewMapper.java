package com.jkojote.weblib.application.views.book;

import com.jkojote.library.persistence.MapCache;
import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.views.author.AuthorView;
import com.jkojote.weblib.utils.MapCacheImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Component("bookViewMapper")
public class BookViewMapper implements RowMapper<BookView> {

    private static final String QUERY_AUTHOR_VIEWS =
        "SELECT " +
          "author.id, author.firstName, author.middleName, author.lastName " +
        "FROM Author author " +
        "INNER JOIN WorkAuthor workAuthor " +
          "ON workAuthor.authorId = author.id " +
        "WHERE workAuthor.workId = ?";

    private static final String URL = Shared.HOST + "books/";

    private static final String LISE_URL = Shared.LISE + "rest/instances/";

    private MapCache<Long, List<AuthorView>> authorViews;

    private JdbcTemplate jdbcTemplate;

    private RowMapper<AuthorView> authorViewMapper;

    @Autowired
    public BookViewMapper(JdbcTemplate jdbcTemplate,
                          @Qualifier("authorViewMapper")
                          RowMapper<AuthorView> authorViewMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorViewMapper = authorViewMapper;
        this.authorViews = new MapCacheImpl<>(256);
        ForkJoinPool.commonPool().execute(this::cleanCache);
    }

    @Override
    public BookView mapRow(ResultSet rs, int rowNum) throws SQLException {
        String
            title = rs.getString("work.title"),
            format = rs.getString("bookInstance.format");
        long
            instanceId = rs.getLong("bookInstance.id"),
            workId = rs.getLong("work.id");
        Object ratingObj = rs.getObject("ratings.averageRating");
        float rating;
        if (ratingObj == null)
            rating = -1;
        else
            rating = (Float) ratingObj;
        List<AuthorView> authorViews = getAuthorViews(workId);
        return BookView.BookViewBuilder.aBookView()
                .withTitle(title)
                .withInstanceId(instanceId)
                .withFormat(format)
                .withAverageRating(rating)
                .withAuthors(authorViews)
                .withUrl(URL + instanceId)
                .withFileUrl(LISE_URL + instanceId + "/file")
                .withImageUrl(LISE_URL + instanceId + "/cover")
                .build();
    }

    private void cleanCache() {
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 2);
                this.authorViews.clean();
            } catch (InterruptedException e) {

            }
        }
    }

    private List<AuthorView> getAuthorViews(long workId) {
        List<AuthorView> res = authorViews.get(workId);
        if (res != null)
            return res;
        res = jdbcTemplate.query(QUERY_AUTHOR_VIEWS, authorViewMapper, workId);
        authorViews.put(workId, res);
        return res;
    }
}
