package com.sahin.library_management.component;

import com.sahin.library_management.config.BasicSecurityConfig;
import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.helper.PageHelper;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.auth.LoginModel;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.mapper.AccountMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Async
@ConditionalOnMissingBean(BasicSecurityConfig.class)
@Profile("dev")
public class Simulator {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    private static final String LOGIN_URI = "http://localhost:7001/api/auth/login";
    private static final String BOOKS_URI = "http://localhost:7001/api/books";
    private static final String ITEMS_URI = "http://localhost:7001/api/book-items";
    private static final String LOANING_URI = "http://localhost:7001/api/book-items/loan";
    private static final String RESERVING_URI = "http://localhost:7001/api/book-items/reserve";

    private static final String FIXED_PW = "1234";

    @Scheduled(initialDelay = 10000, fixedRate = 2000)
    void test() {
        Member member = getRandomMember();
        String token = getToken(member.getLibraryCard().getBarcode()).get(0);

       // getBookById(token, 1L);
        getAllBooks(token);


    }

    private void getRandomMemberProcess() {
        // get book by id
        // get all books
        // get item by barcode
        // get item by book id
        // renew
        // checkout
        // return
        // reserve
    }

    private Book getBookById(String token, Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Book> responseEntity = restTemplate.exchange(BOOKS_URI + "/get/" + id, HttpMethod.GET, entity, Book.class);
        return responseEntity.getBody();
    }

    private PageHelper getAllBooks(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", token);
        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        Map<String,String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "100000");
        params.put("sort", "id,DESC");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PageHelper> responseEntity = restTemplate.exchange(BOOKS_URI + "/search", HttpMethod.POST, entity, PageHelper.class, params);
        return responseEntity.getBody();
    }

    private void getItemByBarcode() {

    }

    private void getItemByBookId() {

    }

    private void renewLoaning() {

    }

    private void checkoutBook() {

    }

    private void returnBook() {

    }

    private void reserveBook() {

    }

    private List<String> getToken(String barcode) {
        LoginModel loginModel = new LoginModel();
        loginModel.setBarcode(barcode);
        loginModel.setPassword(FIXED_PW);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<LoginModel> entity = new HttpEntity<>(loginModel, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(LOGIN_URI, HttpMethod.POST, entity, String.class);
        return responseEntity.getHeaders().get("Authorization");
    }


    private Member getRandomMember() {
        List<AccountEntity> entities = accountRepository.getAll(AccountFor.MEMBER);
        AccountEntity entity = entities.get(new Random().nextInt(entities.size()));
        return accountMapper.toMemberModel(entity, new CyclePreventiveContext());
    }

    private Librarian getRandomLibrarian() {
        List<AccountEntity> entities = accountRepository.getAll(AccountFor.LIBRARIAN);
        AccountEntity entity = entities.get(new Random().nextInt(entities.size()));
        return accountMapper.toLibrarianModel(entity, new CyclePreventiveContext());
    }
}
