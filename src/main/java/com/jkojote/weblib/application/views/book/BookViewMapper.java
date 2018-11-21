package com.jkojote.weblib.application.views.book;

import com.jkojote.library.persistence.MapCache;
import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.views.author.AuthorView;
import com.jkojote.weblib.application.utils.MapCacheImpl;
import com.neovisionaries.i18n.LanguageCode;
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
class BookViewMapper implements RowMapper<BookView> {

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
        String title = rs.getString("book_title");
        long bookId = rs.getLong("book_id");
        LanguageCode lang = LanguageCode.getByCode(rs.getString("book_lang"));
        long workId = rs.getLong("work_id");
        float average = rs.getFloat("rating_average");
        long instanceId = rs.getLong("book_instance_id");
        List<AuthorView> authors = getAuthorViews(workId);
        String coverUrl;
        if (instanceId == 0) {
            coverUrl = Shared.HOST + "res/no-image-available.jpg";
        } else {
            coverUrl = new StringBuilder(Shared.LISE)
                    .append("rest/instances/")
                    .append(instanceId)
                    .append("/cover")
                    .toString();
        }
        average = average == 0 ? -1 : average;
        return BookView.BookViewBuilder.aBookView()
                .withId(bookId)
                .withAverageRating(average)
                .withAuthors(authors)
                .withLangCode(lang.toString())
                .withImageUrl(coverUrl)
                .withUrl(Shared.HOST + "books/" + bookId)
                .withTitle(title)
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
