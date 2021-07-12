package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra.enums.BookStatus;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.mapper.BookItemStateMapper;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.account.Member;
import com.sahin.lms.infra.model.book.BookItemState;
import com.sahin.lms.infra.model.book.BookLoaning;
import com.sahin.lms.infra.model.book.BookReserving;
import com.sahin.lms.loan_service.client.LibraryFeignClient;
import com.sahin.lms.loan_service.repository.BookItemStateRepository;
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
    protected BookItemStateRepository bookItemStateRepository;
    protected BookItemStateMapper bookItemStateMapper;

    @Autowired
    public final void setBookLoaningService(BookLoaningService bookLoaningService) {
        this.bookLoaningService = bookLoaningService;
    }

    @Autowired
    public final void setBookItemStateMapper(BookItemStateMapper bookItemStateMapper) {
        this.bookItemStateMapper = bookItemStateMapper;
    }

    @Autowired
    public final void setBookItemStateRepository(BookItemStateRepository repository) {
        this.bookItemStateRepository = repository;
    }

    @Autowired
    public final void setBookReservingService(BookReservingService bookReservingService) {
        this.bookReservingService = bookReservingService;
    }

    @Autowired
    public final void setLibraryFeignClient(LibraryFeignClient libraryFeignClient) {
        this.libraryFeignClient = libraryFeignClient;
    }

    protected void updateStatus(BookItemState bookItemState, BookStatus newStatus) {
        bookItemState.setStatus(newStatus);
        BookItemStateEntity entity = bookItemStateMapper.toEntity(bookItemState, new CyclePreventiveContext());
        bookItemStateRepository.save(entity);
    }

    public abstract BookLoaning checkOutBookItem(BookItemState bookItemState, Member member);
    public abstract BookReserving reserveBookItem(BookItemState bookItemState, Member member);
    public abstract void returnBookItem(BookLoaning bookLoaning, Member member);
    public abstract BookLoaning renewBookItem(BookLoaning bookLoaning, Member member);

    protected boolean invalidAccount(Member member) {
        return !member.getLibraryCard().getActive();
    }

    protected boolean willExceedMaxNumberOfCheckedOutBooks(Member member) {
        return bookLoaningService.countActiveLoansByMember(member) >= MAX_NO_OF_CHECKEDOUT_BOOKS;
    }

    protected boolean loanedFor(Member member, BookLoaning bookLoaning) {
        return member.getId().equals(bookLoaning.getMemberId());
    }

    protected boolean notHaveReservationFor(BookReserving bookReserving, Member member) {
        return !bookReserving.getMemberId().equals(member.getId());
    }

    protected boolean isValidReservation(BookReserving bookReserving) {
        return bookReserving.getDueDate() != null &&
                bookReserving.getDueDate().compareTo(Instant.now().toEpochMilli()) >= 0;
    }

    protected BookLoaning buildBookLoaning(BookItemState bookItemState, Member member) {
        return BookLoaning.builder()
                .bookItemState(bookItemState)
                .loanedAt(Instant.now().toEpochMilli())
                .dueDate(LocalDateTime.now().plusDays(MAX_DAYS_FOR_KEEPING_A_BOOK).toEpochSecond(ZoneOffset.UTC) * 1000)
                .memberId(member.getId())
                .returnedAt(null)
                .build();
    }

    protected BookReserving buildBookReserving(BookItemState bookItemState, Member member) {
        return BookReserving.builder()
                .bookItemState(bookItemState)
                .memberId(member.getId())
                .reservedAt(Instant.now().toEpochMilli())
                .expectedLoanAt(findExpectedReturnDate(bookItemState))
                .dueDate(null)
                .build();
    }

    private Long findExpectedReturnDate(BookItemState bookItemState) {
        Optional<BookLoaning> bookLoaning = bookLoaningService.findLastByItem(bookItemState);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        return bookLoaning.get().getDueDate();
    }
}