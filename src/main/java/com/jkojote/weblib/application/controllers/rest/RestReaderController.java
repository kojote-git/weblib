package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jkojote.library.domain.model.book.instance.BookInstance;
import com.jkojote.library.domain.model.reader.Reader;
import com.jkojote.library.domain.shared.domain.DomainRepository;
import com.jkojote.types.Email;
import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.security.AuthorizationException;
import com.jkojote.weblib.application.security.AuthorizationService;
import com.jkojote.weblib.application.security.NoSuchReaderExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.errorResponse;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseJson;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseMessage;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/rest/readers")
public class RestReaderController {

    private AuthorizationService authorizationService;

    private DomainRepository<Reader> readerRepository;

    private DomainRepository<BookInstance> bookInstanceRepository;

    private JsonParser jsonParser;

    @Autowired
    public RestReaderController(@Qualifier("readerRepository")
                                DomainRepository<Reader> readerRepository,
                                @Qualifier("bookInstanceRepository")
                                DomainRepository<BookInstance> bookInstanceRepository,
                                AuthorizationService authorizationService) {
        this.readerRepository = readerRepository;
        this.authorizationService = authorizationService;
        this.bookInstanceRepository = bookInstanceRepository;
        this.jsonParser = new JsonParser();
    }

    @PostMapping("creation")
    public ResponseEntity<String> creation(HttpServletRequest req) {
        String email = req.getHeader("Email");
        String password = req.getHeader("Password");
        List<Reader> readers = readerRepository.findAll(reader ->
                reader.getEmail().equals(Email.of(email)));
        if (readers.size() != 0)
            return errorResponse("reader with this email already exists", BAD_REQUEST);
        Reader reader = Reader.ReaderBuilder.aReader()
                .withId(readerRepository.nextId())
                .withEmail(Email.of(email))
                .withPassword(password)
                .withTimeRegistered(LocalDateTime.now())
                .build();
        readerRepository.save(reader);
        return responseJson("{\"id\":" + reader.getId() + "}", CREATED);
    }

    @PostMapping("authorization")
    public ResponseEntity<String> authorization(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String email = req.getHeader("Email");
            String password = req.getHeader("Password");
            if (email == null || password == null)
                return errorResponse("no credentials are present", BAD_REQUEST);
            String token = authorizationService.authorize(email, password);
            Cookie emailCookie = new Cookie("email", email);
            Cookie accessTokenCookie = new Cookie("accessToken", token);
            emailCookie.setPath("/weblib");
            accessTokenCookie.setPath("/weblib");
            resp.addCookie(emailCookie);
            resp.addCookie(accessTokenCookie);
            return responseMessage("authorized", OK);
        } catch (NoSuchReaderExistsException e1) {
            return errorResponse("no such reader exists", NOT_FOUND);
        } catch (AuthorizationException e) {
            return errorResponse("wrong credentials", UNAUTHORIZED);
        }
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getHeader("Email");
        String accessToken = req.getHeader("Access-token");
        authorizationService.logout(email);
        Cookie emailCookie = new Cookie("email", email);
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        emailCookie.setMaxAge(0);
        emailCookie.setPath(Shared.HOST);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath(Shared.HOST);
        resp.addCookie(emailCookie);
        resp.addCookie(accessTokenCookie);
        return responseMessage("logged out", OK);
    }


    @PutMapping("downloading")
    @CrossOrigin
    public ResponseEntity<String> addDownload(HttpServletRequest req)
    throws IOException  {
        String accessToken = req.getHeader("Access-token");
        String email = req.getHeader("Email");
        if (authorizationService.checkToken(email, accessToken))
            return errorResponse("authorization required", UNAUTHORIZED);
        Reader reader = readerRepository.findAll(r ->
                r.getEmail().equals(Email.of(email))).get(0);
        try (BufferedReader r = req.getReader()) {
            JsonObject json = jsonParser.parse(r).getAsJsonObject();
            long id = json.get("instanceId").getAsLong();
            BookInstance bookInstance = bookInstanceRepository.findById(id);
            reader.addToDownloadHistory(bookInstance);
            return responseMessage("download's been saved", OK);
        }
    }

}
