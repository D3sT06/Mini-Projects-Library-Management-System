package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.LogTopic;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.service.BookReservingService;
import com.sahin.library_management.service.MemberLogService;
import com.sahin.library_management.swagger.controller.BookItemReserveSwaggerApi;
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
    private MemberLogService memberLogService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<BookReserving> reserveBookItem(@AuthenticationPrincipal LibraryCard libraryCard, @RequestParam("itemId") String bookItemBarcode) {
        BookReserving bookReserving = bookReservingService.reserveBookItem(bookItemBarcode, libraryCard.getBarcode());

        memberLogService.send(LogTopic.BOOK_RESERVATION, new MemberLog.Builder()
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
