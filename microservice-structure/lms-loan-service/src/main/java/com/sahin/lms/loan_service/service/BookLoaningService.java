package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.ApiKeyConfig;
import com.sahin.lms.infra_entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra_entity.loan.jpa.BookLoaningEntity;
import com.sahin.lms.infra_enum.BookStatus;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.account.Member;
import com.sahin.lms.infra_model.loan.BookItemState;
import com.sahin.lms.infra_model.loan.BookLoaning;
import com.sahin.lms.loan_service.client.AccountFeignClient;
import com.sahin.lms.loan_service.client.LibraryFeignClient;
import com.sahin.lms.loan_service.mapper.BookItemStateMapper;
import com.sahin.lms.loan_service.mapper.BookLoaningMapper;
import com.sahin.lms.loan_service.repository.BookLoaningRepository;
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
public class BookLoaningService {

    @Autowired
    private LibraryFeignClient libraryFeignClient;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonBookItemStateService commonBookItemStateService;

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
    public BookLoaning checkOutBookItem(String bookItemBarcode, String memberBarcode) {

        Member member = accountFeignClient.getMemberByBarcode(apiKeyConfig.getValue(), memberBarcode).getBody();

        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookItem bookItem = libraryFeignClient.getBookItemByBarcode(TokenUtils.getToken(), bookItemBarcode).getBody();

        if (bookItem == null)
            throw new MyRuntimeException("Book item is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookItemState bookItemState = commonBookItemStateService.findById(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookItemState.getStatus());

        return stateService.checkOutBookItem(bookItemState, member);
    }

    @Transactional
    public void returnBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = accountFeignClient.getMemberByBarcode(apiKeyConfig.getValue(), memberBarcode).getBody();

        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookLoaning bookLoaning = getBookLoaning(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookLoaning.getBookItemState().getStatus());

        stateService.returnBookItem(bookLoaning, member);
    }

    @Transactional
    public BookLoaning renewBookItem(String bookItemBarcode, String memberBarcode) {
        Member member = accountFeignClient.getMemberByBarcode(apiKeyConfig.getValue(), memberBarcode).getBody();

        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        BookLoaning bookLoaning = getBookLoaning(bookItemBarcode);
        BookItemStateService stateService = serviceMap.get(bookLoaning.getBookItemState().getStatus());

        return stateService.renewBookItem(bookLoaning, member);
    }

    @Transactional
    public BookLoaning create(BookLoaning bookLoaning) {
        if (bookLoaning.getId() != null)
            throw new MyRuntimeException("Book reservation to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        Member member = accountFeignClient.getMemberById(apiKeyConfig.getValue(), bookLoaning.getMemberId()).getBody();
        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);


        BookLoaningEntity entity = bookLoaningMapper.toEntity(bookLoaning);
        entity = bookLoaningRepository.save(entity);
        BookLoaning loaning = bookLoaningMapper.toModel(entity);

        notificationService.createLoanNotifications(loaning, member.getEmail());
        return loaning;
    }

    @Transactional
    public void update(BookLoaning bookLoaning) {
        if (bookLoaning.getId() == null)
            throw new MyRuntimeException("Book reservation to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!bookLoaningRepository.findById(bookLoaning.getId()).isPresent())
            throw new MyRuntimeException("Book reservation with id \"" + bookLoaning.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        Member member = accountFeignClient.getMemberById(apiKeyConfig.getValue(), bookLoaning.getMemberId()).getBody();
        if (member == null)
            throw new MyRuntimeException("Member is unavailable", HttpStatus.SERVICE_UNAVAILABLE);

        notificationService.deleteLoanNotifications(bookLoaning, member.getEmail());

        BookLoaningEntity entity = bookLoaningMapper.toEntity(bookLoaning);
        bookLoaningRepository.save(entity);
    }

    @Transactional
    public Optional<BookLoaning> findLastByItem(BookItemState bookItemState) {
        BookItemStateEntity bookItemEntity = bookItemStateMapper.toEntity(bookItemState, new CyclePreventiveContext());
        Optional<BookLoaningEntity> bookLoaningEntity = bookLoaningRepository
                .findTopByBookItemStateItemBarcodeOrderByLoanedAtDesc(bookItemEntity.getItemBarcode());
        return Optional.ofNullable(bookLoaningMapper.toModel(bookLoaningEntity.orElse(null)));
    }

    @Transactional
    public int countActiveLoansByMember(Member member) {
        return bookLoaningRepository.countByMemberIdAndReturnedAtIsNull(member.getId());
    }

    private BookLoaning getBookLoaning(String bookItemBarcode) {
        BookItemState bookItemState = commonBookItemStateService.findById(bookItemBarcode);
        Optional<BookLoaning> bookLoaning = findLastByItem(bookItemState);

        if (!bookLoaning.isPresent())
            throw new MyRuntimeException("NOT FOUND", "Book item has not loaned before.", HttpStatus.BAD_REQUEST);

        return bookLoaning.get();
    }
}
