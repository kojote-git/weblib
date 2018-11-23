package com.jkojote.weblib.application.views.book.extended;

import com.jkojote.library.domain.shared.domain.ViewObject;

public class FormatView implements ViewObject {

    private String format;

    private String url;

    public FormatView(String format, String url) {
        this.format = format;
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public String getUrl() {
        return url;
    }
}
