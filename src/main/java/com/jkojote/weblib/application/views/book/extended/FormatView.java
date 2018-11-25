package com.jkojote.weblib.application.views.book.extended;

import com.jkojote.library.domain.shared.domain.ViewObject;

public class FormatView implements ViewObject {

    private long instanceId;

    private String format;

    private String url;

    public FormatView(String format, String url, long instanceId) {
        this.format = format;
        this.url = url;
        this.instanceId = instanceId;
    }

    public String getFormat() {
        return format;
    }

    public String getUrl() {
        return url;
    }

    public long getInstanceId() {
        return instanceId;
    }
}
