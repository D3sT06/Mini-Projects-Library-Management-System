package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.entity_model.BookReservingEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.mapper.BookItemMapper;
import com.sahin.library_management.mapper.BookReservingMapper;
import com.sahin.library_management.repository.BookReservingRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BookReservingService {

    @Autowired
    private BookItemService bookItemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AvailableBookItemStateService availableBookItemStateService;

    @Autowired
    private LoanedBookItemStateService loanedBookItemStateService;

    @Autowired
    private ReservedAtLoanBookItemStateService reservedAtLoanBookItemStateService;

    @Autowired
    private ReservedAtLibraryBookItemStateService reservedAtLibraryBookItemStateService;

    @Autowired
    private BookReservingRepository bookReservingRepository;

    @Autowired
    private BookReservingMapper bookReservingMapper;

    @Autowired
    private BookItemMapper bookItemMapper;

    @Getter
    private Map<BookStatus, BookItemStateService> serviceMap;

    @PostConstruct
    public void setServiceMap() {
        this.serviceMap = new EnumMap<>(BookStatus.class);
        this.serviceMap.put(BookStatus.AVAILABLE, availableBookItemStateService);
        this.serviceMap.put(BookStatus.LOANED, loanedBookItemStateService);
        this.serviceMap.put(BookStatus.RESERVED_AT_LOAN, reservedAtLoanBookItemStateService);
        this.serviceMap.put(BookStatus.RESERVED_AT_LIBRARY, reservedAtLibraryBookItemStateService);
    }

    @Transactional
    public BookReserving reserveBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = memberService.getMemberByBarcode(memberBarcode);
        BookItem bookItem = bookItemService.getBookItemByBarcode(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookItem.getStatus());

        return stateService.reserveBookItem(bookItem, member);
    }

    @Transactional
    public BookReserving create(BookReserving bookReserving) {
        if (bookReserving.getId() != null)
            throw new MyRuntimeException("Book reservation to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookReservingEntity entity = bookReservingMapper.toEntity(bookReserving);
        entity = bookReservingRepository.save(entity);
        return bookReservingMapper.toModel(entity);
    }

    @Transactional
    public void update(BookReserving bookReserving) {
        if (bookReserving.getId() == null)
            throw new MyRuntimeException("Book reservation to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!bookReservingRepository.findById(bookReserving.getId()).isPresent())
            throw new MyRuntimeException("Book reservation with id \"" + bookReserving.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        BookReservingEntity entity = bookReservingMapper.toEntity(bookReserving);
        bookReservingRepository.save(entity);
    }

    @Transactional
    public Optional<BookReserving> findLastByItem(BookItem bookItem) {

        BookItemEntity bookItemEntity = bookItemMapper.toEntity(bookItem);
        Optional<BookReservingEntity> bookReservingEntity = bookReservingRepository.findTopByBookItemOrderByReservedAtDesc(bookItemEntity);
        return Optional.ofNullable(bookReservingMapper.toModel(bookReservingEntity.orElse(null)));
    }
}
