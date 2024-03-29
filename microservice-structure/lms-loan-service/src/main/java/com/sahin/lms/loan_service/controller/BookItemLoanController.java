package com.sahin.lms.loan_service.controller;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.LogTopic;
import com.sahin.lms.infra_model.loan.BookLoaning;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import com.sahin.lms.infra_service.member_log.service.MemberLogPublisherService;
import com.sahin.lms.loan_service.service.BookLoaningService;
import com.sahin.lms.loan_service.swagger.controller.BookItemLoanSwaggerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/book-items/loan")
@LogExecutionTime
public class BookItemLoanController implements BookItemLoanSwaggerApi {

    @Autowired
    private BookLoaningService bookLoaningService;

    @Autowired
    private MemberLogPublisherService memberLogPublisherService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("renew")
    public ResponseEntity<BookLoaning> renewBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning newBookLoaning = bookLoaningService.renewBookItem(bookItemBarcode, libraryCard.getBarcode());

        memberLogPublisherService.send(LogTopic.BOOK_LOAN, new MemberLog.Builder()
                .action(LogAction.RENEW_ITEM, bookItemBarcode)
                .details("New Loaning id: " + newBookLoaning.getId())
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok(newBookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("check-out")
    public ResponseEntity<BookLoaning> checkOutBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookLoaning bookLoaning = bookLoaningService.checkOutBookItem(bookItemBarcode, libraryCard.getBarcode());

        memberLogPublisherService.send(LogTopic.BOOK_LOAN, new MemberLog.Builder()
                .action(LogAction.CHECKOUT_ITEM, bookItemBarcode)
                .details("Loaning id: " + bookLoaning.getId())
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok(bookLoaning);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("return")
    public ResponseEntity<Void> returnBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        bookLoaningService.returnBookItem(bookItemBarcode, libraryCard.getBarcode());

        memberLogPublisherService.send(LogTopic.BOOK_LOAN, new MemberLog.Builder()
                .action(LogAction.RETURN_ITEM, bookItemBarcode)
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBookLoaning(@RequestBody @Valid BookLoaning bookLoaning) {
        bookLoaningService.update(bookLoaning);
        return ResponseEntity.ok().build();
    }
}
