package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.mapper.AuthorMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Transactional
    public Author createAuthor(Author author) {
        AuthorEntity entity = authorMapper.toEntity(author, new CyclePreventiveContext());
        entity = authorRepository.save(entity);

        return authorMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void updateAuthor(Author author) {
        AuthorEntity entity = authorMapper.toEntity(author, new CyclePreventiveContext());
        authorRepository.save(entity);
    }

    @Transactional
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    @Transactional
    public Author getAuthorById(Long authorId) {
        AuthorEntity authorEntity = authorRepository
                .findById(authorId)
                .get();

        return authorMapper.toModel(authorEntity, new CyclePreventiveContext());
    }

    @Transactional
    public List<Author> getAll() {
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        return authorMapper.toModels(authorEntities, new CyclePreventiveContext());
    }
}
