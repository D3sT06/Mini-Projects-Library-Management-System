package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {
}