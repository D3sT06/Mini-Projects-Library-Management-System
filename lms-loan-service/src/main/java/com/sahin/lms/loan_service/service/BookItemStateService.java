package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.model.account.Member;
import com.sahin.lms.infra.model.book.BookItem;
import com.sahin.lms.infra.model.book.BookLoaning;
import com.sahin.lms.infra.model.book.BookReserving;
import com.sahin.lms.loan_service.client.LibraryFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


public abstract class BookItemStateService {
    protected static final int MAX_NO_OF_CHECKEDOUT_BOOKS = 5;
    protected static final int MAX_DAYS_FOR_KEEPING_A_BOOK = 10;
    protected static final int MAX_DAYS_FOR_WAITING_A_RESERVATION = 2;

    protected BookLoaningService bookLoaningService;
    protected BookReservingService bookReservingService;
    protected LibraryFeignClient libraryFeignClient;

    @Autowired
    public final void setBookLoaningService(BookLoaningService bookLoaningService) {
        this.bookLoaningService = bookLoaningService;
    }

    @Autowired
    public final void setBookReservingService(BookReservingService bookReservingService) {
        this.bookReservingService = bookReservingService;
    }

    @Autowired
    public final void setLibraryFeignClient(LibraryFeignClient libraryFeignClient) {
        this.libraryFeignClient = libraryFeignClient;
    }

    public abstract BookLoaning checkOutBookItem(BookItem bookItem, Member member);
    public abstract BookReserving reserveBookItem(BookItem bookItem, Member member);
    public abstract void returnBookItem(BookLoaning bookLoaning, Member member);
    public abstract BookLoaning renewBookItem(BookLoaning bookLoaning, Member member);

    protected boolean invalidAccount(Member member) {
        return !member.getLibraryCard().getActive();
    }

    protected boolean willExceedMaxNumberOfCheckedOutBooks(Member member) {
        return bookLoaningService.countActiveLoansByMember(member) >= MAX_NO_OF_CHECKEDOUT_BOOKS;
    }

    protected boolean loanedFor(Member member, BookLoaning bookLoaning) {
        return member.getLibraryCard().getBarcode().equals(bookLoaning.getMember().getLibraryCard().getBarcode());
    }

    protected boolean notHaveReservationFor(BookReserving bookReserving, Member member) {
        return !bookReserving.getMember().getLibraryCard().getBarcode().equals(member.getLibraryCard().getBarcode());
    }

    protected boolean isValidReservation(BookReserving bookReserving) {
        return bookReserving.getDueDate() != null &&
                bookReserving.getDueDate().compareTo(Instant.now().toEpochMilli()) >= 0;
    }

    protected BookLoaning buildBookLoaning(BookItem bookItem, Member member) {
        return BookLoaning.builder()
                .bookItem(bookItem)
                .loanedAt(Instant.now().toEpochMilli())
                .dueDate(LocalDateTime.now().plusDays(MAX_DAYS_FOR_KEEPING_A_BOOK).toEpochSecond(ZoneOffset.UTC) * 1000)
                .member(member)
                .returnedAt(null)
                .build();
    }

    protected BookReserving buildBookReserving(BookItem bookItem, Member member) {
        return BookReserving.builder()
                .bookItem(bookItem)
                .member(member)
                .reservedAt(Instant.now().toEpochMilli())
                .expectedLoanAt(findExpectedReturnDate(bookItem))
                .dueDate(null)
                .build();
    }

    private Long findExpectedReturnDate(BookItem bookItem) {
        Optional<BookLoaning> bookLoaning = bookLoaningService.findLastByItem(bookItem);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        return bookLoaning.get().getDueDate();
    }
}