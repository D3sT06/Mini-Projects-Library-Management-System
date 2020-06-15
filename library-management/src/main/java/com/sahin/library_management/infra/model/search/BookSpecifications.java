package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.entity_model.BookEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class BookSpecifications {

    private static final String BOOK_TITLE_FIELD = "title";
    private static final String BOOK_AUTHOR_FIELD = "author";
    private static final String BOOK_CATEGORY_FIELD = "category";

    private BookSpecifications() {}

    public static Specification<BookEntity> create(BookFilter bookFilter) {
        return (root, criteriaQuery, cb) -> {
            Predicate allPredicate = getTitlePredicate(bookFilter, root, cb);
            Optional<Predicate> categoryPredicate = getCategoryPredicate(bookFilter, root, cb);
            Optional<Predicate> authorPredicate = getAuthorPredicate(bookFilter, root, cb);

            if (categoryPredicate.isPresent())
                allPredicate = cb.and(allPredicate, categoryPredicate.get());

            if (authorPredicate.isPresent())
                allPredicate = cb.and(allPredicate, authorPredicate.get());

            return allPredicate;
        };
    }

    private static Predicate getTitlePredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        String title = "";
        if (bookFilter.getTitle().isPresent())
            title = bookFilter.getTitle().get();

        return cb.like(root.get(BOOK_TITLE_FIELD), "%" + title + "%");
    }

    private static Optional<Predicate> getCategoryPredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        if (!bookFilter.getCategories().isPresent())
            return Optional.empty();

        CriteriaBuilder.In<BookCategoryEntity> categoryPredicate = cb.in(root.get(BOOK_CATEGORY_FIELD));

        for (BookCategoryEntity entity : bookFilter.getCategories().get())
            categoryPredicate.value(entity);

        return Optional.of(categoryPredicate);
    }

    private static Optional<Predicate> getAuthorPredicate(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {
        if (!bookFilter.getAuthors().isPresent())
            return Optional.empty();

        CriteriaBuilder.In<AuthorEntity> authorPredicate = cb.in(root.get(BOOK_AUTHOR_FIELD));

        for (AuthorEntity entity : bookFilter.getAuthors().get())
            authorPredicate.value(entity);

        return Optional.of(authorPredicate);
    }
}
