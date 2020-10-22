package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.service.BookReservingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/book-items/reserve")
@LogExecutionTime
public class BookItemReserveController {

    @Autowired
    private BookReservingService bookReservingService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<BookReserving> reserveBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookReserving bookReserving = bookReservingService.reserveBookItem(bookItemBarcode, libraryCard.getBarcode());
        return ResponseEntity.ok(bookReserving);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBookReserving(@RequestBody @Valid BookReserving bookReserving) {
        bookReservingService.update(bookReserving);
        return ResponseEntity.ok().build();
    }
}
