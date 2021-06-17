package com.sahin.lms.infra.service;

import com.sahin.lms.infra.annotation.LogExecutionTime;
import com.sahin.lms.infra.entity.jpa.LibraryCardEntity;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.mapper.LibraryCardMapper;
import com.sahin.lms.infra.model.account.LibraryCard;
import com.sahin.lms.infra.repository.LibraryCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@LogExecutionTime
public class LibraryCardService implements UserDetailsService {

    @Autowired
    private LibraryCardMapper libraryCardMapper;

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    public LibraryCard getLibraryCardByBarcode(String barcode) {
        LibraryCardEntity entity = libraryCardRepository
                .findByBarcode(barcode)
                .orElseThrow(()-> new MyRuntimeException("Member with card barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        return libraryCardMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Override
    public LibraryCard loadUserByUsername(String username) {
        return getLibraryCardByBarcode(username);
    }
}
