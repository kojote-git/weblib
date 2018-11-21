package com.jkojote.weblib.application.views.book;

import com.jkojote.library.clauses.SqlClause;

public final class EmptySqlClause implements SqlClause {

    private static final EmptySqlClause INSTANCE = new EmptySqlClause();

    private EmptySqlClause() { }

    public static final SqlClause getClause() {
        return INSTANCE;
    }

    @Override
    public String asString() {
        return "";
    }
}
