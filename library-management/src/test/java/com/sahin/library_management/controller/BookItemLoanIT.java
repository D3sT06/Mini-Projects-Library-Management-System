package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.*;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.mapper.BookLoaningMapper;
import com.sahin.library_management.repository.BookItemRepository;
import com.sahin.library_management.repository.BookLoaningRepository;
import com.sahin.library_management.repository.BookReservingRepository;
import com.sahin.library_management.repository.LibraryCardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Book Item Loan Endpoints:")
public class BookItemLoanIT {
/*
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected BookLoaningMapper loaningMapper;

    @Autowired
    @Qualifier("authorLoader")
    protected Loader<?> authorLoader;

    @Autowired
    @Qualifier("categoryLoader")
    protected Loader<?> categoryLoader;

    @Autowired
    @Qualifier("bookLoader")
    protected Loader<?> bookLoader;

    @Autowired
    @Qualifier("bookItemLoader")
    protected Loader<?> itemLoader;

    @Autowired
    @Qualifier("rackLoader")
    protected Loader<?> rackLoader;

    @Autowired
    @Qualifier("accountLoader")
    protected Loader<?> librarianLoader;

    @Autowired
    @Qualifier("memberLoader")
    protected Loader<?> memberLoader;

    @Autowired
    @Qualifier("bookLoanLoader")
    protected Loader<?> bookLoanLoader;

    @Autowired
    @Qualifier("bookReserveLoader")
    protected Loader<?> bookReserveLoader;

    @Nested
    @SpringBootTest(classes = LibraryManagementApp.class)
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @DisplayName("When the book item is available")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AvailableBookItem {

        @MockBean
        protected LibraryCardRepository libraryCardRepository;

        @Autowired
        protected BookItemRepository bookItemRepository;

        @Autowired
        protected BookLoaningRepository loaningRepository;

        private boolean constructed = false;

        @PostConstruct
        void setup() {
            if (!constructed) {
                constructed = true;
                authorLoader.loadDb();
                categoryLoader.loadDb();
                bookLoader.loadDb();
                rackLoader.loadDb();
                librarianLoader.loadDb();
                memberLoader.loadDb();
            }

            LibraryCardEntity memberCard = ((MemberEntity) memberLoader.getAll().get(0)).getLibraryCard();
            LibraryCardEntity librarianCard = ((AccountEntity) librarianLoader.getAll().get(0)).getLibraryCard();

            given(libraryCardRepository.findById("member")).willReturn(
                    Optional.of(memberCard));
            given(libraryCardRepository.findById("librarian")).willReturn(
                    Optional.of(librarianCard));
        }

        @AfterAll
        void clear() {
            librarianLoader.clearDb();
            memberLoader.clearDb();
            rackLoader.clearDb();
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @BeforeEach
        void beforeEach() {
            itemLoader.loadDb();

            List<BookItemEntity> bookItems = (List<BookItemEntity>) itemLoader.getAll();
            List<MemberEntity> members = (List<MemberEntity>) memberLoader.getAll();

            // the book item has returned yesterday.
            BookLoaningEntity loaning = new BookLoaningEntity();
            loaning.setBookItem(bookItems.get(bookItems.size()-1));
            loaning.setLoanedAt(Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli());
            loaning.setDueDate(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());
            loaning.setReturnedAt(Instant.now().minus(2, ChronoUnit.DAYS).toEpochMilli());
            loaning.setMember(members.get(members.size()-1));

            loaningRepository.save(loaning);
        }

        @AfterEach
        void afterEach() {
            bookLoanLoader.clearDb();
            itemLoader.clearDb();
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                        value = "member",
                        setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot renew the loaning when the item has not been loaned before")
        @Order(1)
        void renewBookItemForNotLoanedBefore() throws Exception {

            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/loan/renew/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT FOUND", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot renew the loaning when the item has loaned before with someone")
        @Order(2)
        void renewBookItemForLoanedBefore() throws Exception {

            BookItemEntity itemEntity = ((BookLoaningEntity) bookLoanLoader.getAll().get(0)).getBookItem();

            mockMvc
                    .perform(
                            post("/api/book-items/loan/renew/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("ALREADY AVAILABLE", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user can loan it")
        @Order(3)
        void checkOutBookItem() throws Exception {
            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/loan/check-out/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        BookLoaning loaning = objectMapper.readValue(result.getResponse().getContentAsString(), BookLoaning.class);
                        assertNotNull(loaning);
                        assertEquals(BookStatus.LOANED, bookItemRepository.findById(itemEntity.getBarcode()).get().getStatus());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot return it when the item has not loaned before")
        @Order(4)
        void returnBookItemForNotLoanedBefore() throws Exception {
            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/loan/return/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT FOUND", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot return it when the item has loaned before by someone")
        @Order(5)
        void returnBookItemForLoanedBefore() throws Exception {
            BookItemEntity itemEntity = ((BookLoaningEntity) bookLoanLoader.getAll().get(0)).getBookItem();

            mockMvc
                    .perform(
                            post("/api/book-items/loan/return/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("ALREADY RETURNED", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "librarian",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then librarian can update it")
        @Order(6)
        void updateBookItem() throws Exception {
            BookLoaningEntity loaningEntity = (BookLoaningEntity) bookLoanLoader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/book-items/loan/update/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + loaningEntity.getId() +",\n" +
                                            "    \"bookItem\": \n" +
                                            "       { \"barcode\": \"" + loaningEntity.getBookItem().getBarcode() + "\"},\n" +
                                            "    \"member\": \n" +
                                            "       { \"id\": " + loaningEntity.getMember().getId() + "},\n" +
                                            "    \"loanedAt\": " + loaningEntity.getLoanedAt() + ",\n" +
                                            "    \"dueDate\": " + loaningEntity.getDueDate() + ",\n" +
                                            "    \"returnedAt\": " + Instant.now().toEpochMilli() + "\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @SpringBootTest(classes = LibraryManagementApp.class)
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @DisplayName("When the book item is reserved when it is on loan")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReservedAtLoanBookItem {

        @MockBean
        protected LibraryCardRepository libraryCardRepository;

        @Autowired
        protected BookItemRepository bookItemRepository;

        @Autowired
        protected BookLoaningRepository loaningRepository;

        @Autowired
        protected BookReservingRepository reservingRepository;

        private boolean constructed = false;

        @PostConstruct
        void setup() {
            if (!constructed) {
                constructed = true;
                authorLoader.loadDb();
                categoryLoader.loadDb();
                bookLoader.loadDb();
                rackLoader.loadDb();
                librarianLoader.loadDb();
                memberLoader.loadDb();
            }

            LibraryCardEntity memberCard = ((MemberEntity) memberLoader.getAll().get(0)).getLibraryCard();
            LibraryCardEntity librarianCard = ((AccountEntity) librarianLoader.getAll().get(0)).getLibraryCard();

            given(libraryCardRepository.findById("member")).willReturn(
                    Optional.of(memberCard));
            given(libraryCardRepository.findById("librarian")).willReturn(
                    Optional.of(librarianCard));
        }

        @AfterAll
        void clear() {
            librarianLoader.clearDb();
            memberLoader.clearDb();
            rackLoader.clearDb();
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @BeforeEach
        void beforeEach() {
            itemLoader.loadDb();

            List<BookItemEntity> bookItems = (List<BookItemEntity>) itemLoader.getAll();
            List<MemberEntity> members = (List<MemberEntity>) memberLoader.getAll();

            // the book item has returned yesterday.
            BookLoaningEntity loaning = new BookLoaningEntity();
            loaning.setBookItem(bookItems.get(bookItems.size()-1));
            loaning.setLoanedAt(Instant.now().minus(5, ChronoUnit.DAYS).toEpochMilli());
            loaning.setDueDate(Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli());
            loaning.setMember(members.get(members.size()-1));

            loaning.getBookItem().setStatus(BookStatus.RESERVED_AT_LOAN);

            bookItemRepository.save(loaning.getBookItem());
            loaningRepository.save(loaning);
        }

        @AfterEach
        void afterEach() {
            bookLoanLoader.clearDb();
            bookReserveLoader.clearDb();
            itemLoader.clearDb();
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot renew the loaning")
        @Order(1)
        void renewBookItem() throws Exception {

            BookItemEntity itemEntity = ((BookLoaningEntity) bookLoanLoader.getAll().get(0)).getBookItem();

            mockMvc
                    .perform(
                            post("/api/book-items/loan/renew/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("RESERVED", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot loan it")
        @Order(2)
        void checkOutBookItem() throws Exception {
            BookItemEntity itemEntity = ((BookLoaningEntity) bookLoanLoader.getAll().get(0)).getBookItem();

            mockMvc
                    .perform(
                            post("/api/book-items/loan/check-out/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT RETURNED", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user cannot return it when the item does not belong to him")
        @Order(3)
        void returnBookItemWhenNotBelongsToMember() throws Exception {
            BookItemEntity itemEntity = ((BookLoaningEntity) bookLoanLoader.getAll().get(0)).getBookItem();

            mockMvc
                    .perform(
                            post("/api/book-items/loan/return/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("FORBIDDEN", errorResponse.getTitle());
                    });
        }

        @Test
        @WithUserDetails(userDetailsServiceBeanName = "libraryCardService",
                value = "member",
                setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("Then user can return it when the item belongs to him")
        @Order(4)
        void returnBookItemWhenBelongsToMember() throws Exception {
            BookLoaningEntity loaningEntity = (BookLoaningEntity) bookLoanLoader.getAll().get(0);
            loaningEntity.setMember((MemberEntity) memberLoader.getAll().get(0));
            loaningRepository.save(loaningEntity);

            BookItemEntity itemEntity = loaningEntity.getBookItem();

            BookReservingEntity reservingEntity = new BookReservingEntity();
            reservingEntity.setMember((MemberEntity) memberLoader.getAll().get(memberLoader.getAll().size()-1));
            reservingEntity.setBookItem(itemEntity);
            reservingRepository.save(reservingEntity);

            mockMvc
                    .perform(
                            post("/api/book-items/loan/return/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        assertNotNull(loaningRepository.findById(loaningEntity.getId()).get().getReturnedAt());
                        assertNotNull(reservingRepository.findById(reservingEntity.getId()).get().getDueDate());
                        assertEquals(BookStatus.RESERVED_AT_LIBRARY, bookItemRepository.findById(itemEntity.getBarcode()).get().getStatus());
                    });
        }
    }

    class ReservedAtLibraryBookItem {
        void renewBookItem() {

        }

        void checkOutBookItem() {

        }

        void returnBookItem() {

        }

        void updateBookItem() {

        }
    }

    class LoanedBookItem {
        void renewBookItem() {

        }

        void checkOutBookItem() {

        }

        void returnBookItem() {

        }

        void updateBookItem() {

        }
    }*/
}
