package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.service.BookLoaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/book-items/loan")
public class BookItemLoanController {

    @Autowired
    private BookLoaningService bookLoaningService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("renew")
    public ResponseEntity<BookLoaning> renewBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning newBookLoaning = bookLoaningService.renewBookItem(bookItemBarcode, libraryCard.getBarcode());
        return ResponseEntity.ok(newBookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("check-out")
    public ResponseEntity<BookLoaning> checkOutBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning bookLoaning = bookLoaningService.checkOutBookItem(bookItemBarcode, libraryCard.getBarcode());
        return ResponseEntity.ok(bookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("return")
    public ResponseEntity<Void> returnBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        bookLoaningService.returnBookItem(bookItemBarcode, libraryCard.getBarcode());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBookLoaning(@RequestBody @Valid BookLoaning bookLoaning) {
        bookLoaningService.update(bookLoaning);
        return ResponseEntity.ok().build();
    }
}
