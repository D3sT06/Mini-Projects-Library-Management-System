package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.mapper.AuthorMapper;
import com.sahin.library_management.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        AuthorEntity entity = authorMapper.toEntity(author);
        entity = authorRepository.save(entity);
        return authorMapper.toModel(entity);
    }

    @Transactional
    public void updateAuthor(Author author) {
        if (author.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Author to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!authorRepository.findById(author.getId()).isPresent())
            throw setExceptionWhenAuthorNotExist(author.getId());

        AuthorEntity entity = authorMapper.toEntity(author);
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
        AuthorEntity entity = authorRepository
                .findById(authorId)
                .orElseThrow(()-> setExceptionWhenAuthorNotExist(authorId));

        return authorMapper.toModel(entity);
    }

    @Transactional
    public List<Author> getAll() {
        List<AuthorEntity> entities = authorRepository
                .findAll();

        return authorMapper.toModels(entities);
    }

    private MyRuntimeException setExceptionWhenAuthorNotExist(Long authorId) {
        return new MyRuntimeException("NOT FOUND", "Author with id \"" + authorId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
