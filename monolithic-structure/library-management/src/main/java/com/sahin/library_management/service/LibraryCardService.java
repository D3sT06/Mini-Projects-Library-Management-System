package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.jpa.LibraryCardEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.mapper.LibraryCardMapper;
import com.sahin.library_management.repository.jpa.LibraryCardRepository;
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
