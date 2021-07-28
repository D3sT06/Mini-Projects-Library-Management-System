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

        MemberEntity entity1 = new MemberEntity();
        entity1.setName("Serkan");
        entity1.setSurname("Sahin");
        entity1.setEmail("serkans@sahin.com");

        MemberEntity entity2 = new MemberEntity();
        entity2.setName("John");
        entity2.setSurname("Doe");
        entity2.setEmail("johndoe@sahin.com");

        MemberEntity entity3 = new MemberEntity();
        entity3.setName("Alex");
        entity3.setSurname("Watt");
        entity3.setEmail("alexw@sahin.com");


        libraryRepository.save(entity1);
        libraryRepository.save(entity2);
        libraryRepository.save(entity3);
    }

    @Override
    public List<MemberEntity> getAll() {
        return libraryRepository.findAll(MemberEntity.class)
                .stream()
                .map(entity -> (MemberEntity) entity)
                .collect(Collectors.toList());
    }
}
