package com.sahin.lms.account_service.bootstrap;

import java.util.List;

public interface Loader<T> {
    void loadDb();
    void clearDb();
    List<T> getAll();
}
