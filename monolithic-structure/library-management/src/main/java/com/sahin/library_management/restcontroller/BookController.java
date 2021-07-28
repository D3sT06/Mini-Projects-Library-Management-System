package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("create")
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity book) {
        BookEntity createdBook = bookService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("update")
    public ResponseEntity<BookEntity> updateBook(@RequestBody BookEntity book) {
        BookEntity updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("delete/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{bookId}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long bookId) {
        BookEntity book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }
}