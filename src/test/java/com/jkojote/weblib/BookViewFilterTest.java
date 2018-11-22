package com.jkojote.weblib;

import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.utils.ViewFilter;
import com.jkojote.weblib.application.views.book.simple.BookView;
import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class BookViewFilterTest {

    @Autowired
    private ViewFilter<BookView> viewFilter;

    @Autowired
    private JsonConverter<BookView> bookViewJsonConverter;

    private ActionRunner actionRunner = new ActionRunner();

    @Test
    public void findAll_1() {
        String page1 = "order=title,ASC&page=1&pageSize=2";
        String page2 = "order=title,ASC&page=2&pageSize=2";
        String page3 = "order=title,ASC&page=3&pageSize=2";
        String page4 = "order=title,ASC&page=4&pageSize=2";
        printAll(viewFilter.findAll(page1));
        printAll(viewFilter.findAll(page2));
        printAll(viewFilter.findAll(page3));
        printAll(viewFilter.findAll(page4));
        System.out.println("------------------------------");
        String orderTitle = "order=title,ASC";
        printAll(viewFilter.findAll(orderTitle));
    }

    @Test
    public void findAll() {
//        actionRunner.report(1000, this::select4Pages);
    }

    @Test
    public void findAll_2() {
        String select1 = "subject=Programming,Fantasy,Classic,Dystopian&lang=ru&author=23";
        printAll(viewFilter.findAll(select1));

    }

    private void select4Pages() {
        String page1 = "order=title,ASC&page=1&pageSize=2";
        String page2 = "order=title,ASC&page=2&pageSize=2";
        String page3 = "order=title,ASC&page=3&pageSize=2";
        String page4 = "order=title,ASC&page=4&pageSize=2";
        viewFilter.findAll(page1);
        viewFilter.findAll(page2);
        viewFilter.findAll(page3);
        viewFilter.findAll(page4);
    }

    void printAll(List<BookView> bookViews) {
        for (BookView bookView : bookViews) {
            System.out.println(bookViewJsonConverter.convertToString(bookView));
        }
    }
}
