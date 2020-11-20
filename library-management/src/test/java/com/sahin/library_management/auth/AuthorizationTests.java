package com.sahin.library_management.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.auth.LoginModel;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.service.AccountLoginTypeService;
import com.sahin.library_management.service.LibraryCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Authorization tests:")
public class AuthorizationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private LibraryCardService libraryCardService;

    @MockBean
    private AccountLoginTypeService accountLoginTypeService;

    @Nested
    @DisplayName("When we have valid token")
    class ValidToken {
        private LoginModel loginModel = new LoginModel();
        private LibraryCard validCard = new LibraryCard();
        private AccountLoginType loginType = new AccountLoginType();

        @BeforeEach
        void setup() {
            loginModel.setBarcode(UUID.randomUUID().toString());
            loginModel.setPassword("1234");

            loginType.setType(LoginType.PASSWORD);
            loginType.setId(1L);
            loginType.setLibraryCard(validCard);

            validCard.setAccountFor(AccountFor.MEMBER);
            validCard.setActive(true);
            validCard.setBarcode(loginModel.getBarcode());
            validCard.setIssuedAt(Instant.now().getEpochSecond());
            validCard.setPassword(passwordEncoder.encode(loginModel.getPassword()));
            validCard.setLoginTypes(new HashSet<>(Arrays.asList(loginType)));
        }

        @Test
        @DisplayName("Then you can authorize if you have permission")
        void validAccess() throws Exception {
            given(libraryCardService.loadUserByUsername(any())).willReturn(validCard);
            given(accountLoginTypeService.doesExist(anyString(), any())).willReturn(true);

            mockMvc
                    .perform(
                            post("/api/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(loginModel)))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"))
                    .andDo(result -> {
                        String token = result.getResponse().getHeader("Authorization");
                        mockMvc
                                .perform(
                                        get("/api/book-items/get-by-book/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("Authorization", token))
                                .andExpect(status().isOk());
                    });
        }

        @Test
        @DisplayName("Then you cannot authorize if you don't have permission")
        void invalidAccess() throws Exception {
            given(libraryCardService.loadUserByUsername(any())).willReturn(validCard);
            given(accountLoginTypeService.doesExist(anyString(), any())).willReturn(true);

            mockMvc
                    .perform(
                            post("/api/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(loginModel)))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"))
                    .andDo(result -> {
                        String token = result.getResponse().getHeader("Authorization");
                        mockMvc
                                .perform(
                                        get("/api/authors/getAll")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("Authorization", token))
                                .andExpect(status().isForbidden());
                    });
        }
    }
}
