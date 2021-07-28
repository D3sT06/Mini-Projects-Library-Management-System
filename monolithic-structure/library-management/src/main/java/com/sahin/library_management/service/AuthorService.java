package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.repository.jpa.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public AuthorEntity createAuthor(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Transactional
    public void updateAuthor(AuthorEntity author) {
        authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    @Transactional
    public AuthorEntity getAuthorById(Long authorId) {
        return authorRepository
                .findById(authorId)
                .get();
    }

    @Transactional
    public List<AuthorEntity> getAll() {
        return authorRepository.findAll();
    }
}
