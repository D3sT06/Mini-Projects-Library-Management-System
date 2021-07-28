package com.sahin.library_management.infra.viewmapper;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.entity.jpa.BookEntity;
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

    public BookViewModel toViewModel(BookEntity model) {

        BookViewModel viewModel = new BookViewModel();
        viewModel.setId(model.getId());
        viewModel.setTitle(model.getTitle());
        viewModel.setAuthorId(model.getAuthor().getId());
        viewModel.setAuthorFullname(model.getAuthor().getName() + " " + model.getAuthor().getSurname());
        viewModel.setCategoryIds((model.getCategories().stream()
                .map(BookCategoryEntity::getId)
                .collect(Collectors.toSet())));
        viewModel.setCategoryNames(model.getCategories().stream()
                .map(BookCategoryEntity::getName)
                .collect(Collectors.joining(", ")));
        viewModel.setPublicationDate(model.getPublicationDate().toString());

        return viewModel;
    }

    public BookEntity toModel(BookViewModel viewModel) {
        BookEntity model = new BookEntity();
        model.setId(viewModel.getId());
        model.setTitle(viewModel.getTitle());

        LocalDate localDate = LocalDate.parse(viewModel.getPublicationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.setPublicationDate(localDate);
        model.setCategories(new HashSet<>());

        AuthorEntity author = new AuthorEntity();
        author.setId(viewModel.getAuthorId());
        model.setAuthor(author);

        for (Long categoryId : viewModel.getCategoryIds()) {
            BookCategoryEntity category = new BookCategoryEntity();
            category.setId(categoryId);
            model.getCategories().add(category);
        }

        return model;
    }

    public List<BookViewModel> toViewModels(List<BookEntity> models) {

        List<BookViewModel> modelCollection = new ArrayList<>();

        models.forEach(entity -> modelCollection.add(this.toViewModel(entity)));
        return modelCollection;
    }
}
