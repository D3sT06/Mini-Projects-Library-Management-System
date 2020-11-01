package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.service.BookItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/book-items")
@LogExecutionTime
public class BookItemController {

    @Autowired
    private BookItemService bookItemService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<BookItem> createBookItem(@RequestBody @Valid BookItem bookItem) {
        BookItem createdItem = bookItemService.createBookItem(bookItem);
        return ResponseEntity.ok(createdItem);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<BookItem> updateBookItem(@RequestBody @Valid BookItem bookItem) {
        BookItem updatedItem = bookItemService.updateBookItem(bookItem);
        return ResponseEntity.ok(updatedItem);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteBookItemByBarcode(@PathVariable String barcode) {
        bookItemService.deleteBookItemByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @GetMapping("get/{barcode}")
    public ResponseEntity<BookItem> getBookItemByBarcode(@PathVariable String barcode) {
        BookItem bookItem = bookItemService.getBookItemByBarcode(barcode);
        return ResponseEntity.ok(bookItem);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @GetMapping("get-by-book/{bookId}")
    public ResponseEntity<List<BookItem>> getBookItemsByBook(@PathVariable Long bookId) {
        List<BookItem> bookItems = bookItemService.getBookItemByBookId(bookId);
        return ResponseEntity.ok(bookItems);
    }
}
