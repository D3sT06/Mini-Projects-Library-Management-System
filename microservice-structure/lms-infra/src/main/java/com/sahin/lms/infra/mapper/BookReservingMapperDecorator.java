package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.loan.jpa.BookReservingEntity;
import com.sahin.lms.infra.model.book.BookReserving;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookReservingMapperDecorator implements BookReservingMapper {

    @Autowired
    private BookReservingMapper delegate;

    @Autowired
    private BookItemStateMapper itemStateMapper;

    @Override
    public BookReserving toModel(BookReservingEntity entity) {
        BookReserving model = delegate.toModel(entity);
        model.setBookItemState(itemStateMapper.toModel(entity.getBookItemState(), new CyclePreventiveContext()));
        return model;
    }

    @Override
    public BookReservingEntity toEntity(BookReserving model) {
        BookReservingEntity entity = delegate.toEntity(model);
        entity.setBookItemState(itemStateMapper.toEntity(model.getBookItemState(), new CyclePreventiveContext()));
        return entity;
    }
}
