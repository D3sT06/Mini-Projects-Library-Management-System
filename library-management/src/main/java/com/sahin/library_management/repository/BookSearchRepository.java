package com.sahin.library_management.repository;

import com.sahin.library_management.infra.model.search.elasticsearch.BookSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookSearchRepository extends ElasticsearchRepository<BookSearchEntity, Long> {

    List<BookSearchEntity> findByTitle(String title);

}
