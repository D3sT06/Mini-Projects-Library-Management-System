package com.sahin.library_management.service.item_state;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.service.item_state.BookItemStateService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@LogExecutionTime
public class LoanedBookItemStateService extends BookItemStateService {

    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("The book item has already been loaned by someone else", HttpStatus.BAD_REQUEST);
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
        bookItemService.updateBookItem(bookReserving.getBookItem());
        return bookReservingService.create(bookReserving);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        if (!loanedFor(member, bookLoaning))
            throw new MyRuntimeException("You cannot return a book that was not loaned by someone else", HttpStatus.BAD_REQUEST);

        bookLoaning.getBookItem().setStatus(BookStatus.AVAILABLE);
        bookLoaning.setReturnedAt(Instant.now().toEpochMilli());

        bookLoaningService.update(bookLoaning);
        bookItemService.updateBookItem(bookLoaning.getBookItem());
    }

    @Transactional
    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        returnBookItem(bookLoaning, member);
        BookItemStateService itemStateService = bookLoaningService.getServiceMap().get(bookLoaning.getBookItem().getStatus());
        return itemStateService.checkOutBookItem(bookLoaning.getBookItem(), member);
    }
}