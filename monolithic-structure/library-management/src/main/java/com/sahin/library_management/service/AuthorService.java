package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
import com.sahin.library_management.mapper.AuthorMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "authors")
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Resource
    private AuthorService self;

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
        entity = authorRepository.save(entity);
        self.cacheEvict(entity);
    }

    @Transactional
    @CacheEvict(key = "#authorId")
    public void deleteAuthorById(Long authorId) {
        Optional<AuthorEntity> optionalEntity = authorRepository.findById(authorId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenAuthorNotExist(authorId);

        authorRepository.deleteById(authorId);
    }

    @Transactional
    @Cacheable(key = "#authorId")
    public AuthorProjections.AuthorView getAuthorById(Long authorId) {
        return authorRepository
                .findProjectedById(authorId)
                .orElseThrow(()-> setExceptionWhenAuthorNotExist(authorId));
    }

    @Transactional
    public List<AuthorProjections.AuthorView> getAll() {
        List<AuthorProjections.AuthorView> authorViews = authorRepository.findAllProjectedBy();

        for (AuthorProjections.AuthorView view : authorViews)
            self.cache(view);

        return authorViews;
    }

    @CachePut(key = "#view.id")
    public AuthorProjections.AuthorView cache(AuthorProjections.AuthorView view) {
        return view;
    }

    @CacheEvict(key = "#entity.id")
    public void cacheEvict(AuthorEntity entity) {}

    private MyRuntimeException setExceptionWhenAuthorNotExist(Long authorId) {
        return new MyRuntimeException("NOT FOUND", "Author with id \"" + authorId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
