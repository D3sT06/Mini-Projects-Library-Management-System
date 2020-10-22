package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookReserving;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@LogExecutionTime
public class ReservedAtLibraryBookItemStateService extends BookItemStateService {

    @Transactional
    public BookLoaning checkOutBookItem(BookItem bookItem, Member member) {
        if (willExceedMaxNumberOfCheckedOutBooks(member))
            throw new MyRuntimeException("You cannot checkout books more than " + MAX_NO_OF_CHECKEDOUT_BOOKS, HttpStatus.BAD_REQUEST);

        if (invalidAccount(member))
            throw new MyRuntimeException("Your account has been suspended. Please contact library management", HttpStatus.BAD_REQUEST);

        Optional<BookReserving> bookReserving = bookReservingService.findLastByItem(bookItem);
        if (!bookReserving.isPresent())
            throw new MyRuntimeException("Book reservation is not available", HttpStatus.BAD_REQUEST);

        if (!isValidReservation(bookReserving.get())) {
            bookItem.setStatus(BookStatus.AVAILABLE);
            bookItemService.updateBookItem(bookItem);
            BookItemStateService itemStateService = bookLoaningService.getServiceMap().get(bookItem.getStatus());
            return itemStateService.checkOutBookItem(bookItem, member);
        }

        if (notHaveReservationFor(bookReserving.get(), member)) {
            throw new MyRuntimeException("This book item is reserved for someone else.", HttpStatus.BAD_REQUEST);
        }

        BookLoaning bookLoaning = buildBookLoaning(bookItem, member);
        bookLoaning.getBookItem().setStatus(BookStatus.LOANED);

        bookItemService.updateBookItem(bookLoaning.getBookItem());
        return bookLoaningService.create(bookLoaning);
    }

    public BookReserving reserveBookItem(BookItem bookItem, Member member) {
        throw new MyRuntimeException("The book with a barcode of " + bookItem.getBarcode()
                + " has already been reserved by someone else.", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void returnBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("The book item is already returned!",
                HttpStatus.BAD_REQUEST);
    }

    public BookLoaning renewBookItem(BookLoaning bookLoaning, Member member) {
        throw new MyRuntimeException("The book item is reserved by someone else. You cannot renew the loaning for it!",
                HttpStatus.BAD_REQUEST);
    }
}
