package com.sahin.lms.library_service.mapper;

import com.sahin.lms.infra_entity.library.jpa.BookEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.library.model.Book;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookCategoryMapper.class, AuthorMapper.class})
public interface BookMapper {

    Book toModel(BookEntity entity, @Context CyclePreventiveContext context);
    BookEntity toEntity(Book model, @Context CyclePreventiveContext context);

    List<Book> toModels(List<BookEntity> entities, @Context CyclePreventiveContext context);

    default Page<Book> toPages(Page<BookEntity> entities, @Context CyclePreventiveContext context) {
            return new PageImpl<>(toModels(entities.getContent(), context), entities.getPageable(), entities.getTotalElements());
    }
}
