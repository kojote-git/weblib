package com.jkojote.weblib;

import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.utils.ViewFilter;
import com.jkojote.weblib.application.views.subject.SubjectView;
import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class SubjectViewFilterTest {

    @Autowired
    private ViewFilter<SubjectView> viewFilter;

    @Autowired
    private JsonConverter<SubjectView> jsonConverter;

    @Test
    public void findAll() {
        printAll(viewFilter.findAll(""));
    }

    private void printAll(List<SubjectView> subjectViews) {
        for (SubjectView view : subjectViews) {
            System.out.println(jsonConverter.convertToString(view));
        }
    }

}
