package com.jkojote.weblib.application.translators;

import com.jkojote.library.domain.model.author.Author;
import com.jkojote.library.domain.model.book.instance.BookInstance;
import com.jkojote.library.domain.model.reader.Download;
import com.jkojote.library.domain.model.reader.Reader;
import com.jkojote.library.domain.shared.domain.DomainRepository;
import com.jkojote.library.persistence.MapCache;
import com.jkojote.weblib.application.Shared;
import com.jkojote.weblib.application.ViewTranslator;
import com.jkojote.weblib.application.views.author.AuthorView;
import com.jkojote.weblib.application.views.book.BookView;
import com.jkojote.weblib.utils.MapCacheImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static java.util.stream.Collectors.toList;

@Component("bookInstanceToBookViewTranslator")
class BookInstanceToBookViewTranslator
implements ViewTranslator<BookInstance, BookView> {

    private DomainRepository<Reader> readerRepository;

    private ViewTranslator<Author, AuthorView> authorViewTranslator;

    private MapCache<BookInstance, Float> ratingsCache;

    private static final String URL = Shared.HOST + "books/";

    private static final String LISE_URL = Shared.LISE + "rest/instances/";

    @Autowired
    public BookInstanceToBookViewTranslator(
            @Qualifier("readerRepository")
            DomainRepository<Reader> readerRepository,
            @Qualifier("authorToAuthorViewTranslator")
            ViewTranslator<Author, AuthorView> authorViewTranslator) {
        this.readerRepository = readerRepository;
        this.ratingsCache = new MapCacheImpl<>();
        this.authorViewTranslator = authorViewTranslator;
        ForkJoinPool.commonPool().execute(this::cleanCacheTask);
    }

    @Override
    public BookView translate(BookInstance bookInstance) {
        float averageRating = calculateAverageRating(bookInstance);
        List<AuthorView> authorViews = authorViewTranslator
                .batchTranslate(bookInstance.getBook().getBasedOn().getAuthors());
        String url = URL + bookInstance.getId();
        String liseUrl = LISE_URL + bookInstance.getId();
        return BookView.BookViewBuilder.aBookView()
                .withTitle(bookInstance.getBook().getBasedOn().getTitle())
                .withInstanceId(bookInstance.getId())
                .withImageUrl(liseUrl + "/cover")
                .withFileUrl(liseUrl + "/file")
                .withUrl(url)
                .withFormat(bookInstance.getFormat().asString())
                .withAverageRating(averageRating)
                .withAuthors(authorViews)
                .build();
    }

    private float calculateAverageRating(BookInstance bookInstance) {
        Float rating = ratingsCache.get(bookInstance);
        if (rating != null)
            return rating;
        List<Download> downloads = readerRepository.findAll().stream()
                .flatMap(r -> r.getDownloads().stream())
                .filter(d -> d.getInstance().equals(bookInstance))
                .collect(toList());
        if (downloads.size() == 0) {
            ratingsCache.put(bookInstance, -1f);
            return -1;
        }
        long ratingSum = downloads.stream()
                .mapToLong(Download::getReaderRating)
                .filter(v -> v != -1)
                .reduce(0, (a, v) -> a + v);
        rating = (float) ratingSum / downloads.size();
        ratingsCache.put(bookInstance, rating);
        return rating;
    }

    private void cleanCacheTask() {
        while (true) {
            try {
                Thread.sleep(120 * 1000);
                ratingsCache.clean();
            } catch (InterruptedException e) {

            }
        }
    }
}
