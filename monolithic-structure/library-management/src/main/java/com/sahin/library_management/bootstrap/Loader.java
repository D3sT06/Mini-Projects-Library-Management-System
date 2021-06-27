package com.sahin.library_management.bootstrap;

import java.util.List;

public interface Loader<T> {
    void loadDb();
    void clearDb();
    List<T> getAll();
}
