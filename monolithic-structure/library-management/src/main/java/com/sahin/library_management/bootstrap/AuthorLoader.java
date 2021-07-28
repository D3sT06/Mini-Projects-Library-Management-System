package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorLoader implements Loader<AuthorEntity> {

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public void loadDb() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setName("Jose");
        author1.setSurname("Saramago");

        AuthorEntity author2 = new AuthorEntity();
        author2.setName("John");
        author2.setSurname("Steinbeck");

        AuthorEntity author3 = new AuthorEntity();
        author3.setName("George");
        author3.setSurname("Orwell");

        AuthorEntity author4 = new AuthorEntity();
        author4.setName("Stephen");
        author4.setSurname("Hawking");

        libraryRepository.save(author1);
        libraryRepository.save(author2);
        libraryRepository.save(author3);
        libraryRepository.save(author4);
    }

    @Override
    public void clearDb() {
        List<AuthorEntity> entities = this.getAll();
        entities.forEach(entity -> libraryRepository.deleteById(entity.getBarcode(), AuthorEntity.class));
    }

    public List<AuthorEntity> getAll() {
        return libraryRepository.findAll(AuthorEntity.class)
                .stream()
                .map(entity -> (AuthorEntity) entity)
                .collect(Collectors.toList());
    }
}
