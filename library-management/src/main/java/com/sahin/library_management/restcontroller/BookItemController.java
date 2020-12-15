package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.LogTopic;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.service.member_log.MemberLogPublisherService;
import com.sahin.library_management.swagger.controller.BookItemSwaggerApi;
import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.service.BookItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/book-items")
@LogExecutionTime
public class BookItemController implements BookItemSwaggerApi {

    @Autowired
    private BookItemService bookItemService;

    @Autowired
    private MemberLogPublisherService memberLogPublisherService;

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

        memberLogPublisherService.send(LogTopic.BOOK_ITEM, new MemberLog.Builder()
                .action(LogAction.GET_ITEM, barcode)
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok(bookItem);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @GetMapping("get-by-book/{bookId}")
    public ResponseEntity<List<BookItem>> getBookItemsByBook(@PathVariable Long bookId) {
        List<BookItem> bookItems = bookItemService.getBookItemByBookId(bookId);

        memberLogPublisherService.send(LogTopic.BOOK_ITEM, new MemberLog.Builder()
                .action(LogAction.GET_ITEM_BY_BOOK, bookId.toString())
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok(bookItems);
    }
}
