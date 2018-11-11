package com.jkojote.weblib.application.translators;

import com.jkojote.library.domain.model.author.Author;
import com.jkojote.weblib.application.ViewTranslator;
import com.jkojote.weblib.application.views.AuthorView;
import org.springframework.stereotype.Component;

@Component("authorToAuthorViewTranslator")
class AuthorToAuthorViewTranslator implements ViewTranslator<Author, AuthorView> {

    @Override
    public AuthorView translate(Author author) {
        return AuthorView.AuthorViewBuilder.anAuthorView()
                .withId(author.getId())
                .withFirstName(author.getName().getFirstName())
                .withMiddleName(author.getName().getMiddleName())
                .withLastName(author.getName().getLastName())
                .build();
    }
}
