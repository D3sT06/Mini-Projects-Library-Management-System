package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookLoaningEntity;
import com.sahin.library_management.infra.model.book.BookLoaning;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookItemMapper.class, AccountMapper.class})
public interface BookLoaningMapper {

    BookLoaning toModel(BookLoaningEntity entity);
    BookLoaningEntity toEntity(BookLoaning model);

    List<BookLoaning> toModels(List<BookLoaningEntity> entities);
}