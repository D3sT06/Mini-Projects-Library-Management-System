package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class LibraryRepository {

    public Map<Long, AuthorEntity> authorsMap = new HashMap<>();
    public Map<Long, BookCategoryEntity> categoriesMap = new HashMap<>();
    public Map<String, BookItemEntity> bookItemsMap = new HashMap<>();
    public Map<Long, BookEntity> booksMap = new HashMap<>();
    public Map<Long, AccountEntity> membersMap = new HashMap<>();
    public Map<Long, RackEntity> racksMap = new HashMap<>();

}
