package com.sahin.lms.infra.model.search;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Getter
@AllArgsConstructor
public abstract class BookPredicate {

    private BookFilter bookFilter;
    private Root<BookEntity> root;
    private CriteriaBuilder cb;

    protected abstract Optional<Predicate> getPredicate();

}
