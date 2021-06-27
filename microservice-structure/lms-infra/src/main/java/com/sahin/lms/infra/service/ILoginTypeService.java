package com.sahin.lms.infra.service;

import com.sahin.lms.infra.enums.LoginType;
import com.sahin.lms.infra.model.account.AccountLoginType;

public interface ILoginTypeService {
    AccountLoginType findByType(String id, LoginType facebook);
    boolean doesExist(String barcode, LoginType password);
}
