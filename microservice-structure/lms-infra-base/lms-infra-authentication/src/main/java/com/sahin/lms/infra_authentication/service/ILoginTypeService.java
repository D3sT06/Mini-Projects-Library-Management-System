package com.sahin.lms.infra_authentication.service;


import com.sahin.lms.infra_authorization.model.AccountLoginType;
import com.sahin.lms.infra_enum.LoginType;

public interface ILoginTypeService {
    AccountLoginType findByType(String id, LoginType facebook);
    boolean doesExist(String barcode, LoginType password);
}
