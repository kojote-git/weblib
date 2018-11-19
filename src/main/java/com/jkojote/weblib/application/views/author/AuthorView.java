package com.jkojote.weblib.application.views.author;

public class AuthorView {

    private String url;

    private long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private AuthorView() { }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public static final class AuthorViewBuilder {

        private String url;

        private long id;

        private String firstName;

        private String middleName;

        private String lastName;

        private AuthorViewBuilder() {
        }

        public static AuthorViewBuilder anAuthorView() {
            return new AuthorViewBuilder();
        }

        public AuthorViewBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public AuthorViewBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public AuthorViewBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthorViewBuilder withMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public AuthorViewBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthorView build() {
            AuthorView authorView = new AuthorView();
            authorView.url = this.url;
            authorView.lastName = this.lastName;
            authorView.firstName = this.firstName;
            authorView.middleName = this.middleName;
            authorView.id = this.id;
            return authorView;
        }
    }
}
