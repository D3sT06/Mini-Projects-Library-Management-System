package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra.enums.BookStatus;
import com.sahin.lms.infra.mapper.BookItemStateMapper;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.book.BookItemState;
import com.sahin.lms.loan_service.repository.BookItemStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommonBookItemStateService {

    protected BookItemStateRepository repository;
    protected BookItemStateMapper mapper;

    public BookItemState findById(String itemBarcode) {
        Optional<BookItemStateEntity> optionalEntity = this.repository.findById(itemBarcode);

        if (optionalEntity.isPresent()) {
            return mapper.toModel(optionalEntity.get(), new CyclePreventiveContext());
        }

        BookItemStateEntity newEntity = new BookItemStateEntity();
        newEntity.setItemBarcode(itemBarcode);
        newEntity.setStatus(BookStatus.AVAILABLE);
        return mapper.toModel(newEntity, new CyclePreventiveContext());
    }
}
