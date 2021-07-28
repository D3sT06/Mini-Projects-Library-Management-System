package com.sahin.library_management.bootstrap;


import com.sahin.library_management.infra.entity.MemberEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountLoader implements Loader<MemberEntity> {

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public void loadDb() {
        loadMembers();
    }

    private void loadMembers() {

        MemberEntity librarianEntity1 = new MemberEntity();
        librarianEntity1.setName("Serkan");
        librarianEntity1.setSurname("Sahin");
        librarianEntity1.setEmail("serkans@sahin.com");

        MemberEntity librarianEntity2 = new MemberEntity();
        librarianEntity2.setName("John");
        librarianEntity2.setSurname("Doe");
        librarianEntity2.setEmail("johndoe@sahin.com");

        MemberEntity librarianEntity3 = new MemberEntity();
        librarianEntity3.setName("Alex");
        librarianEntity3.setSurname("Watt");
        librarianEntity3.setEmail("alexw@sahin.com");


        libraryRepository.save(librarianEntity1);
        libraryRepository.save(librarianEntity2);
        libraryRepository.save(librarianEntity3);
    }

    @Override
    public void clearDb() {
        List<MemberEntity> memberEntities = this.getAll();
        memberEntities.forEach(entity -> libraryRepository.deleteById(entity.getBarcode(), MemberEntity.class));
    }

    @Override
    public List<MemberEntity> getAll() {
        return libraryRepository.findAll(MemberEntity.class)
                .stream()
                .map(entity -> (MemberEntity) entity)
                .collect(Collectors.toList());
    }
}
