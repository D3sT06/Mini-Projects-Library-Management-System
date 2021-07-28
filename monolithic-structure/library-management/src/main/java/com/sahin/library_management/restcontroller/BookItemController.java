package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.service.BookItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book-items")
public class BookItemController {

    @Autowired
    private BookItemService bookItemService;

    @PostMapping("create")
    public ResponseEntity<BookItemEntity> createBookItem(@RequestBody BookItemEntity bookItem) {
        BookItemEntity createdItem = bookItemService.createBookItem(bookItem);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("update")
    public ResponseEntity<BookItemEntity> updateBookItem(@RequestBody BookItemEntity bookItem) {
        BookItemEntity updatedItem = bookItemService.updateBookItem(bookItem);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteBookItemByBarcode(@PathVariable String barcode) {
        bookItemService.deleteBookItemByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{barcode}")
    public ResponseEntity<BookItemEntity> getBookItemByBarcode(@PathVariable String barcode) {
        BookItemEntity bookItem = bookItemService.getBookItemByBarcode(barcode);
        return ResponseEntity.ok(bookItem);
    }

    @GetMapping("get-by-book/{bookId}")
    public ResponseEntity<List<BookItemEntity>> getBookItemsByBook(@PathVariable Long bookId) {
        List<BookItemEntity> bookItems = bookItemService.getBookItemByBookId(bookId);
        return ResponseEntity.ok(bookItems);
    }
}
