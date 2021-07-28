package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.entity.jpa.BookEntity;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/model")
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

        BookEntity bookModel = bookViewModelMapper.toModel(book);
        bookService.createBook(bookModel);

        List<BookEntity> booksList = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(booksList));

        return "redirect:/model/books";
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(BookViewModel book, @PathVariable Long id, Model model) {

        BookEntity bookModel = bookViewModelMapper.toModel(book);

        bookModel.setId(bookService.getBookById(id).getId());
        bookService.updateBook(bookModel);

        List<BookEntity> books = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(books));

        return "redirect:/model/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable Long id, Model model) {

        bookService.deleteBookById(id);

        List<BookEntity> books = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(books));

        return "redirect:/model/books";
    }

    @GetMapping("")
    public String redirectToBooks() {
        return "redirect:/model/books";
    }

    @GetMapping({"books", "books.html"})
    public String books(Model model) {
        List<BookEntity> books = bookService.getAll();

        books.forEach(book -> {

            AuthorEntity author = authorService.getAuthorById(book.getAuthor().getId());
            book.setAuthor(author);
            Set<BookCategoryEntity> categories = new HashSet<>(book.getCategories());

            book.setCategories(new HashSet<>());
            categories.forEach(category -> {
                BookCategoryEntity bookCategory = categoryService.getCategoryById(category.getId());
                book.getCategories().add(bookCategory);
            });
        });
        model.addAttribute("books", bookViewModelMapper.toViewModels(books));

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

        BookEntity bookModel = bookService.getBookById(id);

        AuthorEntity author = authorService.getAuthorById(bookModel.getAuthor().getId());

        bookModel.setAuthor(author);
        Set<BookCategoryEntity> categories = new HashSet<>(bookModel.getCategories());

        bookModel.setCategories(new HashSet<>());
        categories.forEach(category -> {
            BookCategoryEntity bookCategory = categoryService.getCategoryById(category.getId());
            bookModel.getCategories().add(bookCategory);
        });

        model.addAttribute("book", bookViewModelMapper.toViewModel(bookModel));
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("categories", categoryService.getAll());

        return "update-book";
    }
}