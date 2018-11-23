package com.jkojote.weblib.application.views.book.extended;

import com.jkojote.library.domain.shared.domain.ViewObject;
import com.jkojote.weblib.application.views.author.AuthorView;
import com.jkojote.weblib.application.views.book.simple.BookView;
import com.neovisionaries.i18n.LanguageCode;

import java.util.List;

public class ExtendedBookView implements ViewObject {

    private BookView base;

    private List<String> subjects;

    private List<FormatView> formatViews;

    private String descriptionUrl;


    private ExtendedBookView(BookView bookView) {
        this.base = bookView;
    }

    public BookView getBase() {
        return base;
    }

    public List<FormatView> getFormatViews() {
        return formatViews;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }


    public static final class ExtendedBookViewBuilder {

        private BookView base;

        private List<String> subjects;

        private List<FormatView> formatViews;

        private String descriptionUrl;

        private boolean autoClear;

        private ExtendedBookViewBuilder(boolean autoClear) {
            this.autoClear = autoClear;
        }

        public static ExtendedBookViewBuilder builder(boolean autoClear) {
            return new ExtendedBookViewBuilder(autoClear);
        }

        public ExtendedBookViewBuilder withBase(BookView view) {
            this.base = view;
            return this;
        }

        public ExtendedBookViewBuilder withSubjects(List<String> subjects) {
            this.subjects = subjects;
            return this;
        }

        public ExtendedBookViewBuilder withFormats(List<FormatView> formatViews) {
            this.formatViews = formatViews;
            return this;
        }

        public ExtendedBookViewBuilder withDescriptionUrl(String descriptionUrl) {
            this.descriptionUrl = descriptionUrl;
            return this;
        }

        public ExtendedBookView build() {
            ExtendedBookView res = new ExtendedBookView(base);
            res.formatViews = this.formatViews;
            res.descriptionUrl = this.descriptionUrl;
            res.subjects = this.subjects;
            if (autoClear) {
                clear();
            }
            return res;
        }

        public void clear() {
            base = null;
            formatViews = null;
            descriptionUrl = null;
            subjects = null;
        }
    }
}
