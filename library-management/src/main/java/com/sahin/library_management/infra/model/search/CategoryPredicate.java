package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryPredicate extends BookPredicate {

    private static final String BOOK_CATEGORY_FIELD = "categories";
    private static final String CATEGORY_ID_FIELD = "id";

    public CategoryPredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        super(bookFilter, root, cb);
    }

    @Override
    public Optional<Predicate> getPredicate() {
        if (!getBookFilter().getCategoryIds().isPresent())
            return Optional.empty();

        Join<BookEntity, BookCategoryEntity> join = getRoot().join(BOOK_CATEGORY_FIELD);

        Predicate predicate = join.get(CATEGORY_ID_FIELD).in(
                getBookFilter().getCategoryIds().get());

        return Optional.of(predicate);
    }
}
