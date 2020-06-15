package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.service.BookLoaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/book-items/loan")
public class BookItemLoanController {
    @Autowired
    private BookLoaningService bookLoaningService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("renew")
    public ResponseEntity<BookLoaning> renewBookItem(@RequestParam("memberId") String memberBarcode, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning newBookLoaning = bookLoaningService.renewBookItem(bookItemBarcode, memberBarcode);
        return ResponseEntity.ok(newBookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("check-out")
    public ResponseEntity<BookLoaning> checkOutBookItem(@RequestParam("memberId") String memberBarcode, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning bookLoaning = bookLoaningService.checkOutBookItem(bookItemBarcode, memberBarcode);
        return ResponseEntity.ok(bookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("return")
    public ResponseEntity<Void> returnBookItem(@RequestParam("memberId") String memberBarcode, @RequestParam("itemId") String bookItemBarcode) {
        bookLoaningService.returnBookItem(bookItemBarcode, memberBarcode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBookLoaning(@RequestBody BookLoaning bookLoaning) {
        bookLoaningService.update(bookLoaning);
        return ResponseEntity.ok().build();
    }
}
