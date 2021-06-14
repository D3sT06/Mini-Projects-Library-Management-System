package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.BookEntity;
import com.sahin.lms.infra.model.book.Book;
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
