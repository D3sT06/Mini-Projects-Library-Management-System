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

@Service
@LogExecutionTime
public class AvailableBookItemStateService extends BookItemStateService {

    @Override
    @Transactional
    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("FORBIDDEN", "You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.FORBIDDEN);

        if (invalidAccount(member))
            throw new MyRuntimeException("INVALID ACCOUNT", "Your account has been suspended. Please contact library management", HttpStatus.FORBIDDEN);

        BookLoaning bookLoaning = buildBookLoaning(bookItem, member);
        bookLoaning.getBookItem().setStatus(BookStatus.LOANED);

        libraryFeignClient.updateBookItem(bookLoaning.getBookItem());
        return bookLoaningService.create(bookLoaning);
    }

    @Override
    public BookReserving reserveBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("ALREADY AVAILABLE", "The book item is now available. No need to reserve it!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("ALREADY RETURNED", "The book with a barcode of " + bookLoaning.getBookItem().getBarcode()
                + " has already been returned", HttpStatus.BAD_REQUEST);
    }

    @Override
    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("ALREADY AVAILABLE", "The book item is now available. No need to renew it!", HttpStatus.BAD_REQUEST);
    }
}
