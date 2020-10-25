package com.sahin.library_management.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@ConditionalOnProperty(name = "spring.datasource.platform", matchIfMissing = true, havingValue = "value_that_never_appears")
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private AuthorLoader authorLoader;
    private BookItemLoader itemLoader;
    private BookLoader bookLoader;
    private CategoryLoader categoryLoader;
    private AccountLoader accountLoader;
    private RackLoader rackLoader;

    @Override
    public void run(String... args) throws Exception {
          accountLoader.loadDb();
          authorLoader.loadDb();
          categoryLoader.loadDb();
          rackLoader.loadDb();
          bookLoader.loadDb();
          itemLoader.loadDb();
    }
}
