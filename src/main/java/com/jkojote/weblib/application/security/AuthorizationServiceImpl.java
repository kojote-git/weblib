package com.jkojote.weblib.application.security;

import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.domain.model.reader.Reader;
import com.jkojote.library.domain.shared.domain.DomainRepository;
import com.jkojote.types.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
class AuthorizationServiceImpl implements AuthorizationService {

    private DomainRepository<Reader> readerRepository;

    private Map<String, String> authorized;

    @Autowired
    AuthorizationServiceImpl(@Qualifier("readerRepository")
                             DomainRepository<Reader> readerRepository) {
        this.readerRepository = readerRepository;
        this.authorized = new ConcurrentSkipListMap<>();
    }

    @Override
    public String authorize(String email, String password) {
        List<Reader> readers = readerRepository.findAll(reader ->
                reader.getEmail().equals(Email.of(email)));
        if (readers.size() == 0)
            throw new NoSuchReaderExistsException();
        Reader reader = readers.get(0);
        if (!reader.hasSamePassword(password))
            throw new AuthorizationException();
        if (authorized.containsKey(email)) {
            return authorized.get(email);
        }
        String token = Utils.randomAlphaNumeric(32);
        authorized.put(email, token);
        return token;
    }

    @Override
    public void logout(String email) {
        authorized.remove(email);
    }

    @Override
    public String getToken(String email) {
        return authorized.get(email);
    }

    @Override
    public boolean checkToken(String email, String token) {
        String t = authorized.get(email);
        if (t == null)
            return false;
        return t.equals(token);
    }

    @Override
    public boolean authorized(String email) {
        return authorized.containsKey(email);
    }
}
