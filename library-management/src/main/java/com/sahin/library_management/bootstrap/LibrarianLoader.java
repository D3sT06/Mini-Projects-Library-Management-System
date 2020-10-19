package com.sahin.library_management.bootstrap;


import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class LibrarianLoader implements Loader<LibrarianEntity> {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void loadDb() {
        LibraryCardEntity libraryCardEntity1 = new LibraryCardEntity();
        libraryCardEntity1.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity1.setActive(true);
        libraryCardEntity1.setIssuedAt(Instant.now().getEpochSecond());
        libraryCardEntity1.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity2 = new LibraryCardEntity();
        libraryCardEntity2.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity2.setActive(true);
        libraryCardEntity2.setIssuedAt(Instant.now().getEpochSecond());
        libraryCardEntity2.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity3 = new LibraryCardEntity();
        libraryCardEntity3.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity3.setActive(true);
        libraryCardEntity3.setIssuedAt(Instant.now().getEpochSecond());
        libraryCardEntity3.setPassword(passwordEncoder.encode("1234"));

        LibrarianEntity librarianEntity1 = new LibrarianEntity();
        librarianEntity1.setName("Serkan");
        librarianEntity1.setSurname("Sahin");
        librarianEntity1.setEmail("serkans@sahin.com");
        librarianEntity1.setLibraryCard(libraryCardEntity1);

        LibrarianEntity librarianEntity2 = new LibrarianEntity();
        librarianEntity2.setName("John");
        librarianEntity2.setSurname("Doe");
        librarianEntity2.setEmail("johndoe@sahin.com");
        librarianEntity2.setLibraryCard(libraryCardEntity2);

        LibrarianEntity librarianEntity3 = new LibrarianEntity();
        librarianEntity3.setName("Alex");
        librarianEntity3.setSurname("Watt");
        librarianEntity3.setEmail("alexw@sahin.com");
        librarianEntity3.setLibraryCard(libraryCardEntity3);

        librarianRepository.save(librarianEntity1);
        librarianRepository.save(librarianEntity2);
        librarianRepository.save(librarianEntity3);
    }

    @Override
    public void clearDb() {
        librarianRepository.deleteAll();
    }

    @Override
    public List<LibrarianEntity> getAll() {
        return librarianRepository.findAll();
    }
}
