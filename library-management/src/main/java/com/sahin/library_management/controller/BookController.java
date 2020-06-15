package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.search.BookFilter;
import com.sahin.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @GetMapping("get/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @PostMapping("search")
    public ResponseEntity<List<Book>> searchBooks(@RequestBody BookFilter bookFilter) {
        return ResponseEntity.ok(bookService.searchBook(bookFilter));
    }
}
