package com.sahin.lms.apigw.service;

import com.sahin.lms.apigw.mapper.LibraryCardMapper;
import com.sahin.lms.apigw.repository.LibraryCardRepository;
import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_entity.account.jpa.LibraryCardEntity;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
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
