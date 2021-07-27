package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.mapper.AuthorMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Transactional
    public Author createAuthor(Author author) {
        if (author.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Author to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        AuthorEntity entity = authorMapper.toEntity(author, new CyclePreventiveContext());
        entity = authorRepository.save(entity);

        return authorMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void updateAuthor(Author author) {
        if (author.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Author to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!authorRepository.findById(author.getId()).isPresent())
            throw setExceptionWhenAuthorNotExist(author.getId());

        AuthorEntity entity = authorMapper.toEntity(author, new CyclePreventiveContext());
        authorRepository.save(entity);
    }

    @Transactional
    public void deleteAuthorById(Long authorId) {
        Optional<AuthorEntity> optionalEntity = authorRepository.findById(authorId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenAuthorNotExist(authorId);

        authorRepository.deleteById(authorId);
    }

    @Transactional
    public Author getAuthorById(Long authorId) {
        AuthorEntity authorEntity = authorRepository
                .findById(authorId)
                .orElseThrow(()-> setExceptionWhenAuthorNotExist(authorId));

        return authorMapper.toModel(authorEntity, new CyclePreventiveContext());
    }

    @Transactional
    public List<Author> getAll() {
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        return authorMapper.toModels(authorEntities, new CyclePreventiveContext());
    }

    private MyRuntimeException setExceptionWhenAuthorNotExist(Long authorId) {
        return new MyRuntimeException("NOT FOUND", "Author with id \"" + authorId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
