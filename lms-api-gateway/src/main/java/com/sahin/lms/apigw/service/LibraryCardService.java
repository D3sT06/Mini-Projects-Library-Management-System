package com.sahin.lms.apigw.service;

import com.sahin.lms.apigw.repository.LibraryCardRepository;
import com.sahin.lms.infra.annotation.annotation.LogExecutionTime;
import com.sahin.lms.infra.auth.entity.LibraryCardEntity;
import com.sahin.lms.infra.auth.mapper.LibraryCardMapper;
import com.sahin.lms.infra.auth.model.LibraryCard;
import com.sahin.lms.infra.cycle_prevention_mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.error.MyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
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
