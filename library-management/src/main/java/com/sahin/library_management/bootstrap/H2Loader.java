package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.repository.LibrarianRepository;
import com.sahin.library_management.repository.LibraryCardRepository;
import com.sahin.library_management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("test")
public class H2Loader implements CommandLineRunner {

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
          loadLibraryCards();
    }

    private void loadLibraryCards() {
        if (libraryCardRepository.count() == 0) {
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


            LibraryCardEntity libraryCardEntity4 = new LibraryCardEntity();
            libraryCardEntity4.setAccountFor(AccountFor.MEMBER);
            libraryCardEntity4.setActive(true);
            libraryCardEntity4.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity4.setPassword(passwordEncoder.encode("1234"));


            LibraryCardEntity libraryCardEntity5 = new LibraryCardEntity();
            libraryCardEntity5.setAccountFor(AccountFor.MEMBER);
            libraryCardEntity5.setActive(true);
            libraryCardEntity5.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity5.setPassword(passwordEncoder.encode("1234"));


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

            MemberEntity memberEntity1 = new MemberEntity();
            memberEntity1.setName("Wall");
            memberEntity1.setSurname("Orgh");
            memberEntity1.setEmail("wallo@sahin.com");
            memberEntity1.setLibraryCard(libraryCardEntity4);

            MemberEntity memberEntity2 = new MemberEntity();
            memberEntity2.setName("Matt");
            memberEntity2.setSurname("Thogh");
            memberEntity2.setEmail("mattt@sahin.com");
            memberEntity2.setLibraryCard(libraryCardEntity5);

            librarianRepository.save(librarianEntity1);
            librarianRepository.save(librarianEntity2);
            librarianRepository.save(librarianEntity3);
            memberRepository.save(memberEntity1);
            memberRepository.save(memberEntity2);
        }
    }
}
