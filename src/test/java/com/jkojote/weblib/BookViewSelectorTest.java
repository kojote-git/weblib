package com.jkojote.weblib;

import com.jkojote.library.domain.model.book.instance.BookInstance;
import com.jkojote.library.domain.shared.domain.DomainRepository;
import com.jkojote.library.domain.shared.domain.ViewSelector;
import com.jkojote.weblib.application.JsonConverter;
import com.jkojote.weblib.application.ViewTranslator;
import com.jkojote.weblib.application.views.book.BookView;
import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class BookViewSelectorTest {
    @Autowired
    private ViewSelector<BookView> bookViewSelector;

    @Autowired
    private ViewTranslator<BookInstance, BookView> bookViewTranslator;

    @Autowired
    private DomainRepository<BookInstance> bookInstanceRpository;

    @Autowired
    private JsonConverter<BookView> jsonConverter;

    @Test
    public void selectAll() {
        report(1000, bookViewSelector::selectAll);
        report(1000, () -> bookViewTranslator.batchTranslate(bookInstanceRpository.findAll()));
    }

    private void report(int times, Action action) {
        int i = 0;
        long start = 0, end = 0, total = 0;
        while (i < times) {
            start = System.currentTimeMillis();
            action.execute();
            end = System.currentTimeMillis();
            total += end - start;
            i++;
        }
        System.out.println("Time elapsed in average: " + ((float) total) / times + " ms");
    }

    private interface Action {
        void execute();
    }
}
