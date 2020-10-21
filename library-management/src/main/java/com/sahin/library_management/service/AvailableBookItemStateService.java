package com.sahin.library_management.service;

import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookReserving;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AvailableBookItemStateService extends BookItemStateService {

    @Transactional
    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("FORBIDDEN", "You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.FORBIDDEN);

        if (invalidAccount(member))
            throw new MyRuntimeException("INVALID ACCOUNT", "Your account has been suspended. Please contact library management", HttpStatus.FORBIDDEN);

        BookLoaning bookLoaning = buildBookLoaning(bookItem, member);
        bookLoaning.getBookItem().setStatus(BookStatus.LOANED);

        bookItemService.updateBookItem(bookLoaning.getBookItem());
        return bookLoaningService.create(bookLoaning);
    }

    public BookReserving reserveBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("ALREADY AVAILABLE", "The book item is now available. No need to reserve it!", HttpStatus.BAD_REQUEST);
    }

    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("ALREADY RETURNED", "The book with a barcode of " + bookLoaning.getBookItem().getBarcode()
                + " has already been returned", HttpStatus.BAD_REQUEST);
    }

    @Override
    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("ALREADY AVAILABLE", "The book item is now available. No need to renew it!", HttpStatus.BAD_REQUEST);
    }
}
