package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookLoaningEntity;
import com.sahin.library_management.infra.model.book.BookLoaning;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookItemMapper.class, MemberMapper.class})
public interface BookLoaningMapper {

    BookLoaning toModel(BookLoaningEntity entity);
    BookLoaningEntity toEntity(BookLoaning model);
}