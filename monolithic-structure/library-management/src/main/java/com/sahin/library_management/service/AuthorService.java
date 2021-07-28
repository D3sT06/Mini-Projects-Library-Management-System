package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private LibraryRepository libraryRepository;

    public void createAuthor(AuthorEntity author) {
        libraryRepository.save(author);
    }

    public void updateAuthor(AuthorEntity author) {
        libraryRepository.update(author);
    }

    public void deleteAuthorById(String barcode) {
        libraryRepository.deleteById(barcode, AuthorEntity.class);
    }

    public AuthorEntity getAuthorById(String barcode) {
        return (AuthorEntity) libraryRepository
                .findById(barcode, AuthorEntity.class);
    }

    public List<AuthorEntity> getAll() {
        return libraryRepository.findAll(AuthorEntity.class)
                .stream()
                .map(entity -> (AuthorEntity) entity)
                .collect(Collectors.toList());
    }
}
