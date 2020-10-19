package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.repository.LibrarianRepository;
import com.sahin.library_management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MemberTestLoader implements TestLoader {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void loadDb() {
        LibraryCardEntity libraryCardEntity1 = new LibraryCardEntity();
        libraryCardEntity1.setAccountFor(AccountFor.MEMBER);
        libraryCardEntity1.setActive(true);
        libraryCardEntity1.setIssuedAt(Instant.now().getEpochSecond());
        libraryCardEntity1.setPassword(passwordEncoder.encode("1234"));

        LibraryCardEntity libraryCardEntity2 = new LibraryCardEntity();
        libraryCardEntity2.setAccountFor(AccountFor.MEMBER);
        libraryCardEntity2.setActive(true);
        libraryCardEntity2.setIssuedAt(Instant.now().getEpochSecond());
        libraryCardEntity2.setPassword(passwordEncoder.encode("1234"));

        MemberEntity memberEntity1 = new MemberEntity();
        memberEntity1.setName("Wall");
        memberEntity1.setSurname("Orgh");
        memberEntity1.setEmail("wallo@sahin.com");
        memberEntity1.setLibraryCard(libraryCardEntity1);

        MemberEntity memberEntity2 = new MemberEntity();
        memberEntity2.setName("Matt");
        memberEntity2.setSurname("Thogh");
        memberEntity2.setEmail("mattt@sahin.com");
        memberEntity2.setLibraryCard(libraryCardEntity2);

        memberRepository.save(memberEntity1);
        memberRepository.save(memberEntity2);
    }

    @Override
    public void clearDb() {
        memberRepository.deleteAll();
    }
}
