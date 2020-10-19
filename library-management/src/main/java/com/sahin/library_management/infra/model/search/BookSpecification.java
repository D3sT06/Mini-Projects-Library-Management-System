package com.sahin.library_management.infra.model.search;

import com.sahin.library_management.infra.entity_model.BookEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BookSpecification {

    private BookSpecification() {}

    public static Specification<BookEntity> create(BookFilter bookFilter) {
        return ((root, criteriaQuery, criteriaBuilder) -> {

            Predicate allPredicates = null;
            Set<BookPredicate> bookPredicates = createBookPredicates(bookFilter, root, criteriaBuilder);

            for (BookPredicate spec : bookPredicates) {
                Optional<Predicate> predicate = spec.getPredicate();

                if (allPredicates == null) {
                    if (predicate.isPresent()) {
                        allPredicates = predicate.get();
                    }
                } else {
                    if (predicate.isPresent()) {
                        allPredicates = criteriaBuilder.and(allPredicates, predicate.get());
                    }
                }
            }

            return allPredicates;
        });
    }

    private static Set<BookPredicate> createBookPredicates(BookFilter bookFilter, Root<BookEntity> root, CriteriaBuilder cb) {

        TitlePredicate titlePredicate = new TitlePredicate(bookFilter, root, cb);
        CategoryPredicate categoryPredicate = new CategoryPredicate(bookFilter, root, cb);
        AuthorPredicate authorPredicate = new AuthorPredicate(bookFilter, root, cb);

        return new HashSet<>(Arrays.asList(titlePredicate, categoryPredicate, authorPredicate));
    }
}
