package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookCategoryMapper.class, AuthorMapper.class})
public interface BookMapper {
    Book toModel(BookEntity entity, @Context CyclePreventiveContext context);
    BookEntity toEntity(Book model, @Context CyclePreventiveContext context);

    List<Book> toModels(List<BookEntity> entities, @Context CyclePreventiveContext context);
}
