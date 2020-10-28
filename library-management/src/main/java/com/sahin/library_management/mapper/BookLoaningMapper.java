package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookLoaningEntity;
import com.sahin.library_management.infra.model.book.BookLoaning;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookItemMapper.class, AccountMapper.class})
@DecoratedWith(BookLoaningMapperDecorator.class)
public interface BookLoaningMapper {

    @Mapping(target = "bookItem", ignore = true)
    @Mapping(target = "member", ignore = true)
    BookLoaning toModel(BookLoaningEntity entity);

    @Mapping(target = "bookItem", ignore = true)
    @Mapping(target = "member", ignore = true)
    BookLoaningEntity toEntity(BookLoaning model);

    List<BookLoaning> toModels(List<BookLoaningEntity> entities);
}