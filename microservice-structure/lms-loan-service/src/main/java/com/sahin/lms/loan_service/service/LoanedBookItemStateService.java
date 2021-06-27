package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.annotation.LogExecutionTime;
import com.sahin.lms.infra.enums.BookStatus;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.model.account.Member;
import com.sahin.lms.infra.model.book.BookItem;
import com.sahin.lms.infra.model.book.BookLoaning;
import com.sahin.lms.infra.model.book.BookReserving;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@LogExecutionTime
public class LoanedBookItemStateService extends BookItemStateService {

    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("ALREADY LOANED", "The book item has already been loaned by someone else", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public BookReserving reserveBookItem(BookItem bookItem, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.BAD_REQUEST);

        if (invalidAccount(member))
            throw new MyRuntimeException("Your account has been suspended. Please contact library management", HttpStatus.BAD_REQUEST);

        Optional<BookLoaning> bookLoaning = bookLoaningService.findLastByItem(bookItem);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        if (loanedFor(member, bookLoaning.get()))
            throw new MyRuntimeException("You can't reserve a book item which you already have", HttpStatus.BAD_REQUEST);

        BookReserving bookReserving = buildBookReserving(bookItem, member);

        bookReserving.getBookItem().setStatus(BookStatus.RESERVED_AT_LOAN);
        bookItemService.updateStatus(bookReserving.getBookItem().getBarcode(), BookStatus.RESERVED_AT_LOAN);

        return bookReservingService.create(bookReserving);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        if (!loanedFor(member, bookLoaning))
            throw new MyRuntimeException("You cannot return a book that was not loaned by someone else", HttpStatus.BAD_REQUEST);

        bookLoaning.getBookItem().setStatus(BookStatus.AVAILABLE);
        bookLoaning.setReturnedAt(Instant.now().toEpochMilli());

        bookLoaningService.update(bookLoaning);
        bookItemService.updateStatus(bookLoaning.getBookItem().getBarcode(), BookStatus.AVAILABLE);
    }

    @Transactional
    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        returnBookItem(bookLoaning, member);
        BookItemStateService itemStateService = bookLoaningService.getServiceMap().get(bookLoaning.getBookItem().getStatus());
        return itemStateService.checkOutBookItem(bookLoaning.getBookItem(), member);
    }
}