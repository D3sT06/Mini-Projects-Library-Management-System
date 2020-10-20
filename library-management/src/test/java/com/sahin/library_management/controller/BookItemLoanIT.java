package com.sahin.library_management.controller;

import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.mapper.MemberMapper;
import com.sahin.library_management.service.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Book Item Loan Endpoints:")
@ContextConfiguration
public class BookItemLoanIT {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    protected MockMvc mockMvc;

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
    @Qualifier("librarianLoader")
    protected Loader<?> librarianLoader;

    @Autowired
    @Qualifier("memberLoader")
    protected Loader<?> memberLoader;

    @Nested
    @DisplayName("When the book item is available")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AvailableBookItem {

        @BeforeAll
        void setup() {
            authorLoader.loadDb();
            categoryLoader.loadDb();
            bookLoader.loadDb();
            rackLoader.loadDb();
            itemLoader.loadDb();
            librarianLoader.loadDb();
            memberLoader.loadDb();
        }

        @AfterAll
        void clear() {
            librarianLoader.clearDb();
            memberLoader.clearDb();
            itemLoader.clearDb();
            rackLoader.clearDb();
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @Test
//        @WithUserDetails(
//                value = "${((MemberEntity) (memberLoader.getAll().get(0))).getLibraryCard().getBarcode()}",
//                userDetailsServiceBeanName = "libraryCardService"
//        )
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get it by using barcode")
        @Order(1)
        void renewBookItem() throws Exception {
            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            Member member = memberMapper.toModel(((MemberEntity) (memberLoader.getAll().get(0))));
            given(memberService.getMemberByBarcode(any())).willReturn(member);

            mockMvc
                    .perform(
                            post("/api/book-items/loan/renew/")
                                    .param("itemId", itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        void checkOutBookItem() {

        }

        void returnBookItem() {

        }

        void updateBookItem() {

        }
    }

    class ReservedAtLoanBookItem {
        void renewBookItem() {

        }

        void checkOutBookItem() {

        }

        void returnBookItem() {

        }

        void updateBookItem() {

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
    }
}
