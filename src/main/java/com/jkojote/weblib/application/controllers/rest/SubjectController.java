package com.jkojote.weblib.application.controllers.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.utils.MalformedQueryStringException;
import com.jkojote.weblib.application.utils.ViewFilter;
import com.jkojote.weblib.application.views.subject.SubjectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.jkojote.weblib.application.utils.ControllerUtils.errorResponse;
import static com.jkojote.weblib.application.utils.ControllerUtils.responseJson;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/rest/subjects")
public class SubjectController {

    private ViewFilter<SubjectView> subjectViewFilter;

    private JsonConverter<SubjectView> subjectViewJsonConverter;

    @Autowired
    public SubjectController(@Qualifier("subjectViewFilter")
                             ViewFilter<SubjectView> subjectViewFilter,
                             @Qualifier("subjectViewJsonConverter")
                             JsonConverter<SubjectView> subjectViewJsonConverter) {
        this.subjectViewFilter = subjectViewFilter;
        this.subjectViewJsonConverter = subjectViewJsonConverter;
    }

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<String> getAll(HttpServletRequest req) {
        try {
            List<SubjectView> subjects = subjectViewFilter.findAll(req.getQueryString());
            JsonObject json = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            for (SubjectView subj : subjects) {
                jsonArray.add(subjectViewJsonConverter.convertToJson(subj));
            }
            json.add("subjects", jsonArray);
            return responseJson(json.toString(), OK);
        } catch (MalformedQueryStringException e) {
            return errorResponse(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }
}
