package com.sahin.library_management.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private AuthorLoader authorLoader;
    private BookItemLoader itemLoader;
    private BookLoader bookLoader;
    private CategoryLoader categoryLoader;
    private LibrarianLoader librarianLoader;
    private MemberLoader memberLoader;
    private RackLoader rackLoader;

    @Override
    public void run(String... args) throws Exception {
          librarianLoader.loadDb();
          memberLoader.loadDb();
          authorLoader.loadDb();
          categoryLoader.loadDb();
          rackLoader.loadDb();
          bookLoader.loadDb();
          itemLoader.loadDb();
    }
}
