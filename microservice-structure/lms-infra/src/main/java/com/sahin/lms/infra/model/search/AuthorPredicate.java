package com.sahin.lms.infra.model.search;

import com.sahin.lms.infra.entity.library.jpa.AuthorEntity;
import com.sahin.lms.infra.entity.library.jpa.BookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class AuthorPredicate extends BookPredicate {

    private static final String BOOK_AUTHOR_FIELD = "author";

    public AuthorPredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        super(bookFilter, root, cb);
    }

    public Optional<Predicate> getPredicate() {
        if (!getBookFilter().getAuthorIds().isPresent())
            return Optional.empty();

        CriteriaBuilder.In<AuthorEntity> authorPredicate = getCb().in(getRoot().get(BOOK_AUTHOR_FIELD));

        for (Long id : getBookFilter().getAuthorIds().get()) {
            AuthorEntity entity = new AuthorEntity();
            entity.setId(id);
            authorPredicate.value(entity);
        }

        return Optional.of(authorPredicate);
    }
}
