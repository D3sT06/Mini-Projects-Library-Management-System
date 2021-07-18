package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.ApiKeyConfig;
import com.sahin.lms.infra_entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra_entity.loan.jpa.BookReservingEntity;
import com.sahin.lms.infra_enum.BookStatus;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.account.Member;
import com.sahin.lms.infra_model.library.model.BookItem;
import com.sahin.lms.infra_model.loan.BookItemState;
import com.sahin.lms.infra_model.loan.BookReserving;
import com.sahin.lms.loan_service.client.AccountFeignClient;
import com.sahin.lms.loan_service.client.LibraryFeignClient;
import com.sahin.lms.loan_service.mapper.BookItemStateMapper;
import com.sahin.lms.loan_service.mapper.BookReservingMapper;
import com.sahin.lms.loan_service.repository.BookReservingRepository;
import com.sahin.lms.loan_service.utils.TokenUtils;
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
@LogExecutionTime
public class BookReservingService {

    @Autowired
    private LibraryFeignClient libraryFeignClient;

    @Autowired
    private AccountFeignClient accountFeignClient;

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
    private CommonBookItemStateService commonBookItemStateService;

    @Autowired
    private BookItemStateMapper bookItemStateMapper;

    @Autowired
    private ApiKeyConfig apiKeyConfig;

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
        Member member = accountFeignClient.getMemberByBarcode(apiKeyConfig.getValue(), memberBarcode).getBody();

        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookItem bookItem = libraryFeignClient.getBookItemByBarcode(TokenUtils.getToken(), bookItemBarcode).getBody();

        if (bookItem == null)
            throw new MyRuntimeException("Book item is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookItemState bookItemState = commonBookItemStateService.findById(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookItemState.getStatus());

        return stateService.reserveBookItem(bookItemState, member);
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
    public Optional<BookReserving> findLastByItem(BookItemState bookItemState) {

        BookItemStateEntity bookItemEntity = bookItemStateMapper.toEntity(bookItemState, new CyclePreventiveContext());
        Optional<BookReservingEntity> bookReservingEntity = bookReservingRepository.findTopByBookItemStateOrderByReservedAtDesc(bookItemEntity);
        return Optional.ofNullable(bookReservingMapper.toModel(bookReservingEntity.orElse(null)));
    }
}
