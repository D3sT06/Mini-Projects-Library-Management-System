package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_enum.BookStatus;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_model.account.Member;
import com.sahin.lms.infra_model.loan.BookItemState;
import com.sahin.lms.infra_model.loan.BookLoaning;
import com.sahin.lms.infra_model.loan.BookReserving;
import com.sahin.lms.loan_service.utils.DateTimeUtil;
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
    public BookLoaning checkOutBookItem(BookItemState bookItemState, Member member) {
        throw new MyRuntimeException("NOT RETURNED", "The book with a barcode of " + bookItemState.getBarcode()
                + " has not returned yet.", HttpStatus.BAD_REQUEST);
    }

    public BookReserving reserveBookItem(BookItemState bookItemState, Member member) {
        throw new MyRuntimeException("ALREADY RESERVED", "The book with a barcode of " + bookItemState.getBarcode()
                + " has already been reserved by someone else.", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        if (!loanedFor(member, bookLoaning))
            throw new MyRuntimeException("FORBIDDEN", "You cannot return a book that was not loaned by someone else", HttpStatus.FORBIDDEN);

        bookLoaning.getBookItemState().setStatus(BookStatus.RESERVED_AT_LIBRARY);
        bookLoaning.setReturnedAt(Instant.now().toEpochMilli());

        Optional<BookReserving> bookReserving = bookReservingService.findLastByItem(bookLoaning.getBookItemState());
        if (!bookReserving.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book reservation is not available", HttpStatus.BAD_REQUEST);

        long currentPlusWaitingReservation = LocalDateTime.now().plusDays(MAX_DAYS_FOR_WAITING_A_RESERVATION).toEpochSecond(ZoneOffset.UTC) * 1000;
        long duePlusWaitingReservation = DateTimeUtil.toLocalDateTime(bookLoaning.getDueDate()).plusDays(MAX_DAYS_FOR_WAITING_A_RESERVATION).toEpochSecond(ZoneOffset.UTC)*1000;
        bookReserving.get().setDueDate(Math.max(currentPlusWaitingReservation, duePlusWaitingReservation));

        bookReservingService.update(bookReserving.get());
        bookLoaningService.update(bookLoaning);
        this.updateStatus(bookLoaning.getBookItemState(), BookStatus.RESERVED_AT_LIBRARY);
    }

    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("RESERVED", "The book item is reserved by someone else. You cannot renew the loaning for it!",
                HttpStatus.BAD_REQUEST);
    }
}