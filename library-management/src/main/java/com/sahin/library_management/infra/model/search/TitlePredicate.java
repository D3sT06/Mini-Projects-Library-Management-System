package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity.jpa.BookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class TitlePredicate extends BookPredicate {

    private static final String BOOK_TITLE_FIELD = "title";

    public TitlePredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        super(bookFilter, root, cb);
    }

    public Optional<Predicate> getPredicate() {
        String title = "";

        if (getBookFilter().getTitle().isPresent())
            title = getBookFilter().getTitle().get();

        return Optional.of(getCb().like(getRoot().get(BOOK_TITLE_FIELD), "%" + title + "%"));
    }
}
