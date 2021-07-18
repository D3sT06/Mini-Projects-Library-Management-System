package com.sahin.lms.loan_service.mapper;

import com.sahin.lms.infra_entity.loan.jpa.BookLoaningEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.loan.BookLoaning;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookLoaningMapperDecorator implements BookLoaningMapper {

    @Autowired
    private BookLoaningMapper delegate;

    @Autowired
    private BookItemStateMapper itemStateMapper;

    @Override
    public BookLoaning toModel(BookLoaningEntity entity) {
        BookLoaning loaning = delegate.toModel(entity);

        if (loaning == null)
            return null;

        loaning.setBookItemState(itemStateMapper.toModel(entity.getBookItemState(), new CyclePreventiveContext()));
        return loaning;
    }

    @Override
    public BookLoaningEntity toEntity(BookLoaning model) {
        BookLoaningEntity entity = delegate.toEntity(model);
        entity.setBookItemState(itemStateMapper.toEntity(model.getBookItemState(), new CyclePreventiveContext()));
        return entity;
    }
}
