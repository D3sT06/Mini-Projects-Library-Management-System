package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.entity_model.BookLoaningEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.mapper.BookItemMapper;
import com.sahin.library_management.mapper.BookLoaningMapper;
import com.sahin.library_management.mapper.MemberMapper;
import com.sahin.library_management.repository.BookLoaningRepository;
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
public class BookLoaningService {

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
    private BookLoaningRepository bookLoaningRepository;

    @Autowired
    private BookLoaningMapper bookLoaningMapper;

    @Autowired
    private BookItemMapper bookItemMapper;

    @Autowired
    private MemberMapper memberMapper;

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
    public BookLoaning checkOutBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = memberService.getMemberByBarcode(memberBarcode);
        BookItem bookItem = bookItemService.getBookItemByBarcode(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookItem.getStatus());

        return stateService.checkOutBookItem(bookItem, member);
    }

    @Transactional
    public void returnBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = memberService.getMemberByBarcode(memberBarcode);
        BookLoaning bookLoaning = getBookLoaning(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookLoaning.getBookItem().getStatus());

        stateService.returnBookItem(bookLoaning, member);
    }

    @Transactional
    public BookLoaning renewBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = memberService.getMemberByBarcode(memberBarcode);
        BookLoaning bookLoaning = getBookLoaning(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookLoaning.getBookItem().getStatus());

        return stateService.renewBookItem(bookLoaning, member);
    }

    @Transactional
    public BookLoaning create(BookLoaning bookLoaning) {
        if (bookLoaning.getId() != null)
            throw new MyRuntimeException("Book reservation to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        BookLoaningEntity entity = bookLoaningMapper.toEntity(bookLoaning);
        entity = bookLoaningRepository.save(entity);
        return bookLoaningMapper.toModel(entity);
    }

    @Transactional
    public void update(BookLoaning bookLoaning) {
        if (bookLoaning.getId() == null)
            throw new MyRuntimeException("Book reservation to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!bookLoaningRepository.findById(bookLoaning.getId()).isPresent())
            throw new MyRuntimeException("Book reservation with id \"" + bookLoaning.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        BookLoaningEntity entity = bookLoaningMapper.toEntity(bookLoaning);
        bookLoaningRepository.save(entity);
    }

    @Transactional
    public Optional<BookLoaning> findLastByItem(BookItem bookItem) {
        BookItemEntity bookItemEntity = bookItemMapper.toEntity(bookItem);
        Optional<BookLoaningEntity> bookLoaningEntity = bookLoaningRepository.findTopByBookItemOrderByLoanedAtDesc(bookItemEntity);
        return Optional.ofNullable(bookLoaningMapper.toModel(bookLoaningEntity.orElse(null)));
    }

    @Transactional
    public int countActiveLoansByMember(Member member) {
        MemberEntity entity = memberMapper.toEntity(member);
        return bookLoaningRepository.countByMemberAndReturnedAtIsNull(entity);
    }

    private BookLoaning getBookLoaning(String bookItemBarcode) {
        BookItem bookItem = bookItemService.getBookItemByBarcode(bookItemBarcode);
        Optional<BookLoaning> bookLoaning = findLastByItem(bookItem);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        return bookLoaning.get();
    }
}