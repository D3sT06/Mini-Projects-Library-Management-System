package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class LibraryRepository {

    private Map<String, LibraryCardEntity> libraryCards = new HashMap<>();
    private Map<Long, AccountEntity> accounts = new HashMap<>();
    private Map<Long, BookCategoryEntity> categories = new HashMap<>();
    private Map<String, BookItemEntity> bookItems = new HashMap<>();
    private Map<Long, BookEntity> books = new HashMap<>();
    private Map<Long, RackEntity> racks = new HashMap<>();

    private Map<Class, Map> classMap = new HashMap<>();

    @PostConstruct
    void init() {
        classMap.put(LibraryCardEntity.class, libraryCards);
        classMap.put(AccountEntity.class, accounts);
        classMap.put(BookCategoryEntity.class, categories);
        classMap.put(BookEntity.class, books);
        classMap.put(BookItemEntity.class, bookItems);
        classMap.put(RackEntity.class, racks);
    }

    public void save(EntityWithUUID entity) {
        entity.setBarcode(UUID.randomUUID().toString());

        Map map = classMap.get(entity.getClass());
        map.put(entity.getBarcode(), entity);
    }

    public void update(EntityWithUUID entity) {
        Map map = classMap.get(entity.getClass());
        map.put(entity.getBarcode(), entity);
    }

    public void deleteById(String barcode, Class clazz) {
        Map map = classMap.get(clazz);
        map.remove(barcode);
    }

    public List<EntityWithUUID> findAll(Class clazz) {
        Map map = classMap.get(clazz);
        return (List<EntityWithUUID>) map.values();
    }

    public EntityWithUUID findById(String barcode, Class clazz) {
        Map map = classMap.get(clazz);
        return (EntityWithUUID) map.get(barcode);
    }
}
