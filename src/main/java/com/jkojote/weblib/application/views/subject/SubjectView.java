package com.jkojote.weblib.application.views.subject;

import com.jkojote.library.domain.shared.domain.ViewObject;

public class SubjectView implements ViewObject {

    private int id;

    private String subject;

    public SubjectView(int id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }
}
