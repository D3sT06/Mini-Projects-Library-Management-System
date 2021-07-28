package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class LibraryRepository {

    private Map<Long, MemberEntity> accounts = new HashMap<>();
    private Map<Long, AuthorEntity> authors = new HashMap<>();
    private Map<Long, BookCategoryEntity> categories = new HashMap<>();
    private Map<Long, BookEntity> books = new HashMap<>();

    private Map<Class, Map> classMap = new HashMap<>();

    @PostConstruct
    void init() {
        classMap.put(MemberEntity.class, accounts);
        classMap.put(BookCategoryEntity.class, categories);
        classMap.put(BookEntity.class, books);
        classMap.put(AuthorEntity.class, authors);
    }

    public void save(EntityWithUUID entity) {
        entity.setBarcode(UUID.randomUUID().toString());

        Map map = classMap.get(entity.getClass());
        map.put(entity.getBarcode(), entity);
    }

    public List<EntityWithUUID> findAll(Class clazz) {
        Map map = classMap.get(clazz);
        return new ArrayList<EntityWithUUID>(map.values());
    }
}
