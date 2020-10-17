package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.entity_model.BookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class CategoryPredicate extends BookPredicate {

    private static final String BOOK_CATEGORY_FIELD = "category";

    public CategoryPredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        super(bookFilter, root, cb);
    }

    @Override
    public Optional<Predicate> getPredicate() {
        if (!getBookFilter().getCategories().isPresent())
            return Optional.empty();

        CriteriaBuilder.In<BookCategoryEntity> categoryPredicate = getCb().in(getRoot().get(BOOK_CATEGORY_FIELD));

        for (BookCategoryEntity entity : getBookFilter().getCategories().get())
            categoryPredicate.value(entity);

        return Optional.of(categoryPredicate);
    }
}
