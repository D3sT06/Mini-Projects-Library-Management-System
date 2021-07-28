package com.sahin.library_management.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev"})
public class DatabaseLoader implements CommandLineRunner {

    private AuthorLoader authorLoader;
    private BookLoader bookLoader;
    private CategoryLoader categoryLoader;
    private AccountLoader accountLoader;

    public DatabaseLoader(AuthorLoader authorLoader, BookLoader bookLoader, CategoryLoader categoryLoader, AccountLoader accountLoader) {
        this.authorLoader = authorLoader;
        this.bookLoader = bookLoader;
        this.categoryLoader = categoryLoader;
        this.accountLoader = accountLoader;
    }

    @Override
    public void run(String... args) throws Exception {
          accountLoader.loadDb();
          authorLoader.loadDb();
          categoryLoader.loadDb();
          bookLoader.loadDb();
    }
}
