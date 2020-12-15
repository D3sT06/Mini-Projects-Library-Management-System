package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.viewmapper.BookViewModelMapper;
import com.sahin.library_management.infra.viewmodel.BookViewModel;
import com.sahin.library_management.service.AuthorService;
import com.sahin.library_management.service.BookCategoryService;
import com.sahin.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class BookModelController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookCategoryService categoryService;

    @Autowired
    private BookViewModelMapper bookViewModelMapper;

    @PostMapping("/books/create")
    public String createBook(BookViewModel book, Model model) {

        BookEntity entity = bookViewModelMapper.toEntity(book);
        bookService.createBook(entity);

        List<BookEntity> entities = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(entities));

        return "redirect:/books";
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(BookViewModel book, @PathVariable Long id, Model model) {

        BookEntity entity = bookViewModelMapper.toEntity(book);

        entity.setId(bookService.getBookById(id).getId());
        bookService.updateBook(entity);

        List<BookEntity> entities = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(entities));

        return "redirect:/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable Long id, Model model) {

        bookService.deleteBookById(id);

        List<BookEntity> entities = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(entities));

        return "redirect:/books";
    }

    @GetMapping({"books", "books.html"})
    public String books(Model model) {
        List<BookEntity> entities = bookService.getAll();

        entities.forEach(entity -> {
            entity.setAuthor(authorService.getAuthorById(entity.getAuthor().getId()));
            Set<BookCategoryEntity> categories = new HashSet<>(entity.getCategories());

            entity.setCategories(new HashSet<>());
            categories.forEach(category -> entity.getCategories().add(categoryService.getCategoryById(category.getId())));
        });
        model.addAttribute("books", bookViewModelMapper.toViewModels(entities));

        return "books";
    }

    @GetMapping("/books/new")
    public String showSignUpForm(BookViewModel book, ModelMap modelMap) {
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("authors", authorService.getAll());
        modelMap.addAttribute("categories", categoryService.getAll());

        return "add-book";
    }

//    @GetMapping("/books/new")
//    public String showSignUpForm(BookViewModel book, Model model) {
//        model.addAttribute("book", book);
//        model.addAttribute("authors", authorService.getAll());
//        model.addAttribute("categories", categoryService.getAll());
//
//        return "add-book";
//    }

    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        BookEntity entity = bookService.getBookById(id);
        entity.setAuthor(authorService.getAuthorById(entity.getAuthor().getId()));
        Set<BookCategoryEntity> categories = new HashSet<>(entity.getCategories());

        entity.setCategories(new HashSet<>());
        categories.forEach(category -> entity.getCategories().add(categoryService.getCategoryById(category.getId())));

        model.addAttribute("book", bookViewModelMapper.toViewModel(entity));
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("categories", categoryService.getAll());

        return "update-book";
    }
}