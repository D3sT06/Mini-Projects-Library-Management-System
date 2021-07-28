package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.repository.jpa.jpa.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorLoader implements Loader<AuthorEntity> {

    @Autowired
    private AuthorRepository authorRepository;

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

        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        authorRepository.save(author4);
    }

    @Override
    public void clearDb() {
        authorRepository.deleteAll();
    }

    public List<AuthorEntity> getAll() {
        return authorRepository.findAll();
    }
}
