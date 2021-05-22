package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.infra.projections.AuthorProjections;
import com.sahin.library_management.infra.projections.CategoryProjections;
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

        Book bookModel = bookViewModelMapper.toModel(book);
        bookService.createBook(bookModel);

        List<Book> booksList = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(booksList));

        return "redirect:/model/books";
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(BookViewModel book, @PathVariable Long id, Model model) {

        Book bookModel = bookViewModelMapper.toModel(book);

        bookModel.setId(bookService.getBookById(id).getId());
        bookService.updateBook(bookModel);

        List<Book> books = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(books));

        return "redirect:/model/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable Long id, Model model) {

        bookService.deleteBookById(id);

        List<Book> books = bookService.getAll();
        model.addAttribute("books", bookViewModelMapper.toViewModels(books));

        return "redirect:/model/books";
    }

    @GetMapping("")
    public String redirectToBooks() {
        return "redirect:/model/books";
    }

    @GetMapping({"books", "books.html"})
    public String books(Model model) {
        List<Book> books = bookService.getAll();

        books.forEach(book -> {

            AuthorProjections.AuthorView authorView = authorService.getAuthorById(book.getAuthor().getId());
            Author author = new Author();
            author.setId(authorView.getId());
            author.setName(authorView.getName());
            author.setSurname(authorView.getSurname());

            book.setAuthor(author);
            Set<BookCategory> categories = new HashSet<>(book.getCategories());

            book.setCategories(new HashSet<>());
            categories.forEach(category -> {

                CategoryProjections.CategoryView categoryView = categoryService.getCategoryById(category.getId());
                BookCategory bookCategory = new BookCategory();
                bookCategory.setId(categoryView.getId());
                bookCategory.setName(categoryView.getName());

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

        Book bookModel = bookService.getBookById(id);

        AuthorProjections.AuthorView authorView = authorService.getAuthorById(bookModel.getAuthor().getId());
        Author author = new Author();
        author.setId(authorView.getId());
        author.setName(authorView.getName());
        author.setSurname(authorView.getSurname());

        bookModel.setAuthor(author);
        Set<BookCategory> categories = new HashSet<>(bookModel.getCategories());

        bookModel.setCategories(new HashSet<>());
        categories.forEach(category -> {

            CategoryProjections.CategoryView categoryView = categoryService.getCategoryById(category.getId());
            BookCategory bookCategory = new BookCategory();
            bookCategory.setId(categoryView.getId());
            bookCategory.setName(categoryView.getName());

            bookModel.getCategories().add(bookCategory);
        });

        model.addAttribute("book", bookViewModelMapper.toViewModel(bookModel));
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("categories", categoryService.getAll());

        return "update-book";
    }
}