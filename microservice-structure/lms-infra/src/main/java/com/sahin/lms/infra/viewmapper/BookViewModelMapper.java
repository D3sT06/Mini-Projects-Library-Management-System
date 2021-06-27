package com.sahin.lms.infra.viewmapper;

import com.sahin.lms.infra.model.book.Author;
import com.sahin.lms.infra.model.book.Book;
import com.sahin.lms.infra.model.book.BookCategory;
import com.sahin.lms.infra.viewmodel.BookViewModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookViewModelMapper {

    public BookViewModel toViewModel(Book model) {

        BookViewModel viewModel = new BookViewModel();
        viewModel.setId(model.getId());
        viewModel.setTitle(model.getTitle());
        viewModel.setAuthorId(model.getAuthor().getId());
        viewModel.setAuthorFullname(model.getAuthor().getName() + " " + model.getAuthor().getSurname());
        viewModel.setCategoryIds((model.getCategories().stream()
                .map(BookCategory::getId)
                .collect(Collectors.toSet())));
        viewModel.setCategoryNames(model.getCategories().stream()
                .map(BookCategory::getName)
                .collect(Collectors.joining(", ")));
        viewModel.setPublicationDate(model.getPublicationDate().toString());

        return viewModel;
    }

    public Book toModel(BookViewModel viewModel) {
        Book model = new Book();
        model.setId(viewModel.getId());
        model.setTitle(viewModel.getTitle());

        LocalDate localDate = LocalDate.parse(viewModel.getPublicationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.setPublicationDate(localDate);
        model.setCategories(new HashSet<>());

        Author author = new Author();
        author.setId(viewModel.getAuthorId());
        model.setAuthor(author);

        for (Long categoryId : viewModel.getCategoryIds()) {
            BookCategory category = new BookCategory();
            category.setId(categoryId);
            model.getCategories().add(category);
        }

        return model;
    }

    public List<BookViewModel> toViewModels(List<Book> models) {

        List<BookViewModel> modelCollection = new ArrayList<>();

        models.forEach(entity -> modelCollection.add(this.toViewModel(entity)));
        return modelCollection;
    }
}
