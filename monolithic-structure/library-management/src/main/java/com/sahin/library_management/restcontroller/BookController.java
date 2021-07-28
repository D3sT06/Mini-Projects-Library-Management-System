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
    public ResponseEntity<Void> createBook(@RequestBody BookEntity book) {
        bookService.createBook(book);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<BookEntity> updateBook(@RequestBody BookEntity book) {
        bookService.updateBook(book);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable String barcode) {
        bookService.deleteBookById(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{bookId}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable String barcode) {
        BookEntity book = bookService.getBookById(barcode);
        return ResponseEntity.ok(book);
    }
}