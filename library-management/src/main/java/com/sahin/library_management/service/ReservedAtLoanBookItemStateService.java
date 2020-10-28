package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.util.DateTimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@LogExecutionTime
public class ReservedAtLoanBookItemStateService extends BookItemStateService {

    @Transactional
    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("NOT RETURNED", "The book with a barcode of " + bookItem.getBarcode()
                + " has not returned yet.", HttpStatus.BAD_REQUEST);
    }

    public BookReserving reserveBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("ALREADY RESERVED", "The book with a barcode of " + bookItem.getBarcode()
                + " has already been reserved by someone else.", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        if (!loanedFor(member, bookLoaning))
            throw new MyRuntimeException("FORBIDDEN", "You cannot return a book that was not loaned by someone else", HttpStatus.FORBIDDEN);

        bookLoaning.getBookItem().setStatus(BookStatus.RESERVED_AT_LIBRARY);
        bookLoaning.setReturnedAt(Instant.now().toEpochMilli());

        Optional<BookReserving> bookReserving = bookReservingService.findLastByItem(bookLoaning.getBookItem());
        if (!bookReserving.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book reservation is not available", HttpStatus.BAD_REQUEST);

        long currentPlusWaitingReservation = LocalDateTime.now().plusDays(MAX_DAYS_FOR_WAITING_A_RESERVATION).toEpochSecond(ZoneOffset.UTC) * 1000;
        long duePlusWaitingReservation = DateTimeUtil.toLocalDateTime(bookLoaning.getDueDate()).plusDays(MAX_DAYS_FOR_WAITING_A_RESERVATION).toEpochSecond(ZoneOffset.UTC)*1000;
        bookReserving.get().setDueDate(Math.max(currentPlusWaitingReservation, duePlusWaitingReservation));

        bookReservingService.update(bookReserving.get());
        bookLoaningService.update(bookLoaning);
        bookItemService.updateBookItem(bookLoaning.getBookItem());
    }

    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("RESERVED", "The book item is reserved by someone else. You cannot renew the loaning for it!",
                HttpStatus.BAD_REQUEST);
    }
}