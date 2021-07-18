package com.sahin.lms.loan_service.controller;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.LogTopic;
import com.sahin.lms.infra_model.loan.BookReserving;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import com.sahin.lms.infra_service.member_log.service.MemberLogPublisherService;
import com.sahin.lms.loan_service.service.BookReservingService;
import com.sahin.lms.loan_service.swagger.controller.BookItemReserveSwaggerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/book-items/reserve")
@LogExecutionTime
public class BookItemReserveController implements BookItemReserveSwaggerApi {

    @Autowired
    private BookReservingService bookReservingService;

    @Autowired
    private MemberLogPublisherService memberLogPublisherService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<BookReserving> reserveBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookReserving bookReserving = bookReservingService.reserveBookItem(bookItemBarcode, libraryCard.getBarcode());

        memberLogPublisherService.send(LogTopic.BOOK_RESERVATION, new MemberLog.Builder()
                .action(LogAction.RESERVE_ITEM, bookItemBarcode)
                .details("New Reserving id: " + bookReserving.getId())
                .httpStatus(HttpStatus.OK)
                .build()
        );

        return ResponseEntity.ok(bookReserving);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateBookReserving(@RequestBody @Valid BookReserving bookReserving) {
        bookReservingService.update(bookReserving);
        return ResponseEntity.ok().build();
    }
}
