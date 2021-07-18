package com.sahin.lms.library_service.model;

import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import com.sahin.lms.infra_model.library.model.BookFilter;
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

    public abstract Optional<Predicate> getPredicate();

}
