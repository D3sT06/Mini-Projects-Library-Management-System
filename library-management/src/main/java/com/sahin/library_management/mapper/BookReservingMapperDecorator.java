package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.BookReservingEntity;
import com.sahin.library_management.infra.model.book.BookReserving;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookReservingMapperDecorator implements BookReservingMapper {

    @Autowired
    private BookReservingMapper delegate;

    @Autowired
    private BookItemMapper itemMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BookReserving toModel(BookReservingEntity entity) {
        BookReserving model = delegate.toModel(entity);
        model.setBookItem(itemMapper.toModel(entity.getBookItem(), new CyclePreventiveContext()));
        model.setMember(accountMapper.toMemberModel(entity.getMember(), new CyclePreventiveContext()));
        return model;
    }

    @Override
    public BookReservingEntity toEntity(BookReserving model) {
        BookReservingEntity entity = delegate.toEntity(model);
        entity.setBookItem(itemMapper.toEntity(model.getBookItem(), new CyclePreventiveContext()));
        entity.setMember(accountMapper.toEntity(model.getMember(), new CyclePreventiveContext()));
        return entity;
    }
}
