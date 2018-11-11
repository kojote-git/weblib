package com.jkojote.weblib.application.views;


import java.util.List;

public class BookView {

    private String imageUrl;

    private String url;

    private String title;

    private String fileUrl;

    private String format;

    private float averageRating;

    private long instanceId;

    private List<AuthorView> authors;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public String getFormat() {
        return format;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public List<AuthorView> getAuthors() {
        return authors;
    }

    public static final class BookViewBuilder {

        private String imageUrl;

        private String url;

        private String fileUrl;

        private String title;

        private float averageRating;

        private long instanceId;

        private List<AuthorView> authors;

        private String format;

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

        public BookViewBuilder withFormat(String format) {
            this.format = format;
            return this;
        }

        public BookViewBuilder withFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
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

        public BookViewBuilder withInstanceId(long instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public BookViewBuilder withAuthors(List<AuthorView> authors) {
            this.authors = authors;
            return this;
        }

        public BookView build() {
            BookView bookView = new BookView();
            bookView.averageRating = this.averageRating;
            bookView.authors = this.authors;
            bookView.imageUrl = this.imageUrl;
            bookView.instanceId = this.instanceId;
            bookView.title = this.title;
            bookView.url = this.url;
            bookView.fileUrl = this.fileUrl;
            bookView.format = this.format;
            return bookView;
        }
    }
}
