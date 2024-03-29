package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_enum.BookStatus;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_model.account.Member;
import com.sahin.lms.infra_model.loan.BookItemState;
import com.sahin.lms.infra_model.loan.BookLoaning;
import com.sahin.lms.infra_model.loan.BookReserving;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@LogExecutionTime
public class LoanedBookItemStateService extends BookItemStateService {

    public BookLoaning checkOutBookItem(BookItemState bookItemState, Member member) {
        throw new MyRuntimeException("ALREADY LOANED", "The book item has already been loaned by someone else", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public BookReserving reserveBookItem(BookItemState bookItemState, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("FORBIDDEN", "You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.BAD_REQUEST);

        if (invalidAccount(member))
            throw new MyRuntimeException("SUSPENDED", "Your account has been suspended. Please contact library management", HttpStatus.BAD_REQUEST);

        Optional<BookLoaning> bookLoaning = bookLoaningService.findLastByItem(bookItemState);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("NOT LOANED", "Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        if (loanedFor(member, bookLoaning.get()))
            throw new MyRuntimeException("ALREADY LOANED", "You can't reserve a book item which you already have", HttpStatus.BAD_REQUEST);

        BookReserving bookReserving = buildBookReserving(bookItemState, member);

        bookReserving.getBookItemState().setStatus(BookStatus.RESERVED_AT_LOAN);
        this.updateStatus(bookReserving.getBookItemState(), BookStatus.RESERVED_AT_LOAN);
        return bookReservingService.create(bookReserving);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        if (!loanedFor(member, bookLoaning))
            throw new MyRuntimeException("FORBIDDEN", "You cannot return a book that was not loaned by someone else", HttpStatus.BAD_REQUEST);

        bookLoaning.getBookItemState().setStatus(BookStatus.AVAILABLE);
        bookLoaning.setReturnedAt(Instant.now().toEpochMilli());

        bookLoaningService.update(bookLoaning);
        this.updateStatus(bookLoaning.getBookItemState(), BookStatus.AVAILABLE);
    }

    @Transactional
    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        returnBookItem(bookLoaning, member);
        BookItemStateService itemStateService = bookLoaningService.getServiceMap().get(bookLoaning.getBookItemState().getStatus());
        return itemStateService.checkOutBookItem(bookLoaning.getBookItemState(), member);
    }
}