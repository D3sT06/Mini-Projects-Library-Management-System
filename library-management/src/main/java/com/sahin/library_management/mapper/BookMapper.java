package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookCategoryMapper.class, AuthorMapper.class})
public interface BookMapper {
    Book toModel(BookEntity entity);
    BookEntity toEntity(Book model);

    List<Book> toModels(List<BookEntity> entities);
}
