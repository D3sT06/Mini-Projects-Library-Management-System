package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.AccountLoginTypeEntity;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.mapper.AccountLoginTypeMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.AccountLoginTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@LogExecutionTime
public class AccountLoginTypeService {

    @Autowired
    private AccountLoginTypeMapper accountLoginTypeMapper;

    @Autowired
    private AccountLoginTypeRepository accountLoginTypeRepository;

    public boolean doesExist(String barcode, LoginType type) {
        return accountLoginTypeRepository.existsByLibraryCardBarcodeAndType(barcode, type);
    }

    public AccountLoginType findByType(String key, LoginType type) {
        AccountLoginTypeEntity entity = accountLoginTypeRepository.findByTypeSpecificKeyAndType(key, type)
                .orElseThrow(()-> new MyRuntimeException("Member with type key " + key + " for " + type + " not exist!", HttpStatus.BAD_REQUEST));

        return accountLoginTypeMapper.toModel(entity, new CyclePreventiveContext());
    }
}
