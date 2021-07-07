package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.loan.jpa.BookLoaningEntity;
import com.sahin.lms.infra.model.book.BookLoaning;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookLoaningMapperDecorator implements BookLoaningMapper {

    @Autowired
    private BookLoaningMapper delegate;

    @Autowired
    private BookItemMapper itemMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BookLoaning toModel(BookLoaningEntity entity) {
        BookLoaning loaning = delegate.toModel(entity);

        if (loaning == null)
            return null;

        loaning.setBookItem(itemMapper.toModel(entity.getBookItem(), new CyclePreventiveContext()));
        loaning.setMember(accountMapper.toMemberModel(entity.getMember(), new CyclePreventiveContext()));
        return loaning;
    }

    @Override
    public BookLoaningEntity toEntity(BookLoaning model) {
        BookLoaningEntity entity = delegate.toEntity(model);
        entity.setBookItem(itemMapper.toEntity(model.getBookItem(), new CyclePreventiveContext()));
        entity.setMember(accountMapper.toEntity(model.getMember(), new CyclePreventiveContext()));
        return entity;
    }
}
