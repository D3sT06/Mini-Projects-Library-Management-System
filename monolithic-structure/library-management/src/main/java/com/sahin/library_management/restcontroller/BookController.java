package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.search.BookFilter;
import com.sahin.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/books")
@LogExecutionTime
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Book> updateBook(@RequestBody @Valid Book book) {
        Book updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
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
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }


    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @PostMapping("search")
    public ResponseEntity<Page<Book>> getAll(@RequestBody @Valid BookFilter bookFilter, Pageable pageable) {

        Page<Book> books = bookService.searchBook(pageable, bookFilter);
        return ResponseEntity.ok(books);
    }
}