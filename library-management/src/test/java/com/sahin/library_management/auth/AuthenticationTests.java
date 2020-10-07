package com.sahin.library_management.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.auth.LoginModel;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Authentication tests:")
class AuthenticationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private LibraryCardService libraryCardService;

    @Nested
    @DisplayName("When we have valid credentials")
    class ValidCredentials {
        private LoginModel loginModel = new LoginModel();
        private LibraryCard validCard = new LibraryCard();

        @BeforeEach
        void setup() {
            loginModel.setBarcode(UUID.randomUUID().toString());
            loginModel.setPassword("1234");

            validCard.setAccountFor(AccountFor.MEMBER);
            validCard.setActive(true);
            validCard.setBarcode(loginModel.getBarcode());
            validCard.setIssuedAt(Instant.now().getEpochSecond());
            validCard.setPassword(passwordEncoder.encode(loginModel.getPassword()));
        }

        @Test
        @DisplayName("Then you can authenticate successfully")
        void validAccess() throws Exception {
            given(libraryCardService.loadUserByUsername(any())).willReturn(validCard);

            mockMvc
                    .perform(
                            post("/api/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(loginModel)))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"));
        }
    }

    @Nested
    @DisplayName("When we have invalid credentials")
    class InvalidCredentials {
        private LoginModel loginModel = new LoginModel();
        private LibraryCard card = new LibraryCard();

        @BeforeEach
        void setup() {
            loginModel.setBarcode(UUID.randomUUID().toString());
            loginModel.setPassword("1234");

            card.setAccountFor(AccountFor.MEMBER);
            card.setActive(true);
            card.setBarcode(loginModel.getBarcode());
            card.setIssuedAt(Instant.now().getEpochSecond());
            card.setPassword(passwordEncoder.encode(loginModel.getPassword()));
        }

        @Test
        @DisplayName("Then you cannot authenticate because of wrong password")
        void wrongPasswordCannotAuthenticate() throws Exception {
            loginModel.setPassword("wrong password");

            given(libraryCardService.loadUserByUsername(any())).willReturn(card);

            mockMvc
                    .perform(
                            post("/api/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(loginModel)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("When we have invalid account")
    class InvalidAccount {
        private LoginModel loginModel = new LoginModel();
        private LibraryCard card = new LibraryCard();

        @BeforeEach
        void setup() {
            loginModel.setBarcode(UUID.randomUUID().toString());
            loginModel.setPassword("1234");

            card.setAccountFor(AccountFor.MEMBER);
            card.setActive(false);
            card.setBarcode(loginModel.getBarcode());
            card.setIssuedAt(Instant.now().getEpochSecond());
            card.setPassword(passwordEncoder.encode(loginModel.getPassword()));
        }

        @Test
        @DisplayName("Then you cannot authenticate because of deactive account")
        void deactiveUserCannotAuthenticate() throws Exception {
            card.setActive(false);

            given(libraryCardService.loadUserByUsername(any())).willReturn(card);

            mockMvc
                    .perform(
                            post("/api/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(loginModel)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
