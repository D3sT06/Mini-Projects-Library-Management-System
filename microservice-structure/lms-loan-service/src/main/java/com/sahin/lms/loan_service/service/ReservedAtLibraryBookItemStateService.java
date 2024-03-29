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
import java.util.Optional;

@Service
@LogExecutionTime
public class ReservedAtLibraryBookItemStateService extends BookItemStateService {

    @Transactional
    public BookLoaning checkOutBookItem(BookItemState bookItemState, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.BAD_REQUEST);

        if (invalidAccount(member))
            throw new MyRuntimeException("Your account has been suspended. Please contact library management", HttpStatus.BAD_REQUEST);

        Optional<BookReserving> bookReserving = bookReservingService.findLastByItem(bookItemState);
        if (!bookReserving.isPresent())
            throw new MyRuntimeException("Book reservation is not available", HttpStatus.BAD_REQUEST);

        if (!isValidReservation(bookReserving.get())) {
            bookItemState.setStatus(BookStatus.AVAILABLE);
            this.updateStatus(bookItemState, BookStatus.AVAILABLE);
            BookItemStateService itemStateService = bookLoaningService.getServiceMap().get(bookItemState.getStatus());
            return itemStateService.checkOutBookItem(bookItemState, member);
        }

        if (notHaveReservationFor(bookReserving.get(), member)) {
            throw new MyRuntimeException("This book item is reserved for someone else.", HttpStatus.BAD_REQUEST);
        }

        BookLoaning bookLoaning = buildBookLoaning(bookItemState, member);
        bookLoaning.getBookItemState().setStatus(BookStatus.LOANED);
        this.updateStatus(bookLoaning.getBookItemState(), BookStatus.LOANED);
        return bookLoaningService.create(bookLoaning);
    }

    public BookReserving reserveBookItem(BookItemState bookItemState, Member member) {
        throw new MyRuntimeException("The book with a barcode of " + bookItemState.getBarcode()
                + " has already been reserved by someone else.", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("ALREADY RETURNED", "The book item is already returned!",
                HttpStatus.BAD_REQUEST);
    }

    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("RESERVED", "The book item is reserved by someone else. You cannot renew the loaning for it!",
                HttpStatus.BAD_REQUEST);
    }
}
