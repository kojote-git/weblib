package com.jkojote.weblib.application.views.book;


import com.jkojote.library.domain.shared.domain.ViewObject;
import com.jkojote.weblib.application.views.author.AuthorView;

import java.util.List;

public class BookView implements ViewObject {

    public static final String AVG_RATING = "rating.average";

    public static final String TITLE = "book.title";

    public static final String LANGUAGE = "book.lang";

    private long id;

    private String imageUrl;

    private String langCode;

    private String url;

    private String title;

    private float averageRating;

    private List<AuthorView> authors;

    public long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLanguageCode() {
        return langCode;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public List<AuthorView> getAuthors() {
        return authors;
    }

    public static final class BookViewBuilder {

        private String imageUrl;

        private String url;

        private long id;

        private String title;

        private String lang;

        private float averageRating;

        private List<AuthorView> authors;

        private BookViewBuilder() {
        }

        public static BookViewBuilder aBookView() {
            return new BookViewBuilder();
        }

        public BookViewBuilder withImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public BookViewBuilder withUrl(String url) {
            this.url = url;
            return this;
        }


        public BookViewBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public BookViewBuilder withAverageRating(float averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public BookViewBuilder withAuthors(List<AuthorView> authors) {
            this.authors = authors;
            return this;
        }

        public BookViewBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public BookViewBuilder withLangCode(String langCode) {
            this.lang = langCode;
            return this;
        }

        public BookView build() {
            BookView bookView = new BookView();
            bookView.averageRating = this.averageRating;
            bookView.authors = this.authors;
            bookView.imageUrl = this.imageUrl;
            bookView.id = this.id;
            bookView.title = this.title;
            bookView.url = this.url;
            bookView.langCode = lang;
            return bookView;
        }
    }
}
