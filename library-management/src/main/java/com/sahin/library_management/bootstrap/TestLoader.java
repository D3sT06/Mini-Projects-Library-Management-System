package com.sahin.library_management.bootstrap;

import org.springframework.stereotype.Component;

public interface TestLoader {
    void loadDb();
    void clearDb();
}
