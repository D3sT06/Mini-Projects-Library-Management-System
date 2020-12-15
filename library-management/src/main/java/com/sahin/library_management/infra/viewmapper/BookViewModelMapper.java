package com.sahin.library_management.infra.viewmapper;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.viewmodel.BookViewModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookViewModelMapper {

    public BookViewModel toViewModel(BookEntity entity) {

        BookViewModel model = new BookViewModel();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setAuthorId(entity.getAuthor().getId());
        model.setAuthorFullname(entity.getAuthor().getName() + " " + entity.getAuthor().getSurname());
        model.setCategoryIds((entity.getCategories().stream()
                .map(BookCategoryEntity::getId)
                .collect(Collectors.toSet())));
        model.setCategoryNames(entity.getCategories().stream()
                .map(BookCategoryEntity::getName)
                .collect(Collectors.joining(", ")));
        model.setPublicationDate(entity.getPublicationDate().toString());

        return model;
    }

    public BookEntity toEntity(BookViewModel model) {
        BookEntity entity = new BookEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());

        LocalDate localDate = LocalDate.parse(model.getPublicationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        entity.setPublicationDate(localDate);
        entity.setCategories(new HashSet<>());

        AuthorEntity author = new AuthorEntity();
        author.setId(model.getAuthorId());
        entity.setAuthor(author);

        for (Long categoryId : model.getCategoryIds()) {
            BookCategoryEntity category = new BookCategoryEntity();
            category.setId(categoryId);
            entity.getCategories().add(category);
        }

        return entity;
    }

    public List<BookViewModel> toViewModels(List<BookEntity> entities) {

        List<BookViewModel> modelCollection = new ArrayList<>();

        entities.forEach(entity -> modelCollection.add(this.toViewModel(entity)));
        return modelCollection;
    }
}
