package com.sahin.library_management.bootstrap;


import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.entity.LibraryCardEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class AccountLoader implements Loader<AccountEntity> {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void loadDb() {
        loadLibrarians();
        loadMembers();
    }

    private void loadLibrarians() {
        LibraryCardEntity libraryCardEntity1 = new LibraryCardEntity();
        libraryCardEntity1.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity1.setActive(true);
        libraryCardEntity1.setIssuedAt(Instant.now().toEpochMilli());
        libraryCardEntity1.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity2 = new LibraryCardEntity();
        libraryCardEntity2.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity2.setActive(true);
        libraryCardEntity2.setIssuedAt(Instant.now().toEpochMilli());
        libraryCardEntity2.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity3 = new LibraryCardEntity();
        libraryCardEntity3.setAccountFor(AccountFor.LIBRARIAN);
        libraryCardEntity3.setActive(true);
        libraryCardEntity3.setIssuedAt(Instant.now().toEpochMilli());
        libraryCardEntity3.setPassword(passwordEncoder.encode("1234"));

        AccountEntity librarianEntity1 = new AccountEntity();
        librarianEntity1.setName("Serkan");
        librarianEntity1.setSurname("Sahin");
        librarianEntity1.setEmail("serkans@sahin.com");
        librarianEntity1.setType(AccountFor.LIBRARIAN);
        librarianEntity1.setLibraryCard(libraryCardEntity1);

        AccountEntity librarianEntity2 = new AccountEntity();
        librarianEntity2.setName("John");
        librarianEntity2.setSurname("Doe");
        librarianEntity2.setEmail("johndoe@sahin.com");
        librarianEntity2.setType(AccountFor.LIBRARIAN);
        librarianEntity2.setLibraryCard(libraryCardEntity2);

        AccountEntity librarianEntity3 = new AccountEntity();
        librarianEntity3.setName("Alex");
        librarianEntity3.setSurname("Watt");
        librarianEntity3.setEmail("alexw@sahin.com");
        librarianEntity3.setType(AccountFor.LIBRARIAN);
        librarianEntity3.setLibraryCard(libraryCardEntity3);


        accountRepository.save(librarianEntity1);
        accountRepository.save(librarianEntity2);
        accountRepository.save(librarianEntity3);
    }

    private void loadMembers() {
        LibraryCardEntity libraryCardEntity1 = new LibraryCardEntity();
        libraryCardEntity1.setAccountFor(AccountFor.MEMBER);
        libraryCardEntity1.setActive(true);
        libraryCardEntity1.setIssuedAt(Instant.now().toEpochMilli());
        libraryCardEntity1.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity2 = new LibraryCardEntity();
        libraryCardEntity2.setAccountFor(AccountFor.MEMBER);
        libraryCardEntity2.setActive(true);
        libraryCardEntity2.setIssuedAt(Instant.now().toEpochMilli());
        libraryCardEntity2.setPassword(passwordEncoder.encode("1234"));

        AccountEntity memberEntity1 = new AccountEntity();
        memberEntity1.setName("Wall");
        memberEntity1.setSurname("Orgh");
        memberEntity1.setEmail("wallo@sahin.com");
        memberEntity1.setType(AccountFor.MEMBER);
        memberEntity1.setLibraryCard(libraryCardEntity1);

        AccountEntity memberEntity2 = new AccountEntity();
        memberEntity2.setName("Matt");
        memberEntity2.setSurname("Thogh");
        memberEntity2.setEmail("mattt@sahin.com");
        memberEntity2.setType(AccountFor.MEMBER);
        memberEntity2.setLibraryCard(libraryCardEntity2);

        accountRepository.save(memberEntity1);
        accountRepository.save(memberEntity2);
    }

    @Override
    public void clearDb() {
        accountRepository.deleteAll();
    }

    @Override
    public List<AccountEntity> getAll() {
        return accountRepository.findAll();
    }
}
