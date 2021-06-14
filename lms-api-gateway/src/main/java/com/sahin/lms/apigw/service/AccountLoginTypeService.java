package com.sahin.lms.apigw.service;

import com.sahin.lms.apigw.mapper.AccountLoginTypeMapper;
import com.sahin.lms.apigw.repository.AccountLoginTypeRepository;
import com.sahin.lms.infra.annotation.LogExecutionTime;
import com.sahin.lms.infra.entity.jpa.AccountLoginTypeEntity;
import com.sahin.lms.infra.enums.LoginType;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.account.AccountLoginType;
import com.sahin.lms.infra.service.ILoginTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@LogExecutionTime
public class AccountLoginTypeService implements ILoginTypeService {

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
