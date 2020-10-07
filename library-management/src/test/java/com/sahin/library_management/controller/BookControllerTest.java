package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.H2Loader;
import com.sahin.library_management.infra.auth.JwtTokenDecoderService;
import com.sahin.library_management.infra.auth.TokenValidationFilter;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.repository.BookRepository;
import com.sahin.library_management.service.LibraryCardService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Book Endpoints:")
class BookControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Nested
    @DisplayName("When the book is valid")
    class ValidBook {
        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create a book")
        void createBook() throws Exception {
            mockMvc
                    .perform(
                            post("/api/books/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": 1444462452,\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": 3\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book book = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
                        assertNotNull(book);
                        assertNotNull(book.getId());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can update a book")
        void updateBook() throws Exception {
            mockMvc
                    .perform(
                            put("/api/books/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 3,\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": 1444462452,\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": 3\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can delete a book")
        void deleteBookById() throws Exception {

            // create a book with no book items to delete.
            createBook();

            mockMvc
                    .perform(
                            delete("/api/books/delete/6")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get a book")
        void getBookById() throws Exception {
            mockMvc
                    .perform(
                            get("/api/books/get/1")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book book = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
                        assertNotNull(book);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can find all the books without using any filter")
        void searchBooksWithNoFilter() throws Exception {
            mockMvc
                    .perform(
                            post("/api/books/search")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book[] books = objectMapper.readValue(result.getResponse().getContentAsString(), Book[].class);
                        assertEquals(5, books.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can find the book he wants by using filters")
        void searchBooksWithFilter() throws Exception {
            mockMvc
                    .perform(
                            post("/api/books/search")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            "{\n" +
                                                    "    \"title\": \"19\",\n" +
                                                    "    \"categories\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": 1\n" +
                                                    "        } \n" +
                                                    "    ],\n" +
                                                    "    \"authors\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": 3\n" +
                                                    "        }\n" +
                                                    "    ]\n" +
                                                    "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book[] books = objectMapper.readValue(result.getResponse().getContentAsString(), Book[].class);
                        assertEquals(1, books.length);
                    });
        }
    }

    @Nested
    @DisplayName("When the book is invalid")
    class InvalidBook {
        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot create it with id")
        void createBook() throws Exception {
            mockMvc
                    .perform(
                            post("/api/books/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 1,\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": 1444462452,\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": 3\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT CREATED", errorResponse.getTitle());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot update it without id")
        void updateBookWithoutId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/books/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": 1444462452,\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": 3\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT UPDATED", errorResponse.getTitle());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot update it if not found")
        void updateBookWithNonExistingId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/books/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 1000,\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": 1444462452,\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": 3\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT FOUND", errorResponse.getTitle());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot delete it if not found")
        void deleteBookById() throws Exception {

            mockMvc
                    .perform(
                            delete("/api/books/delete/1000")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT FOUND", errorResponse.getTitle());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then a user cannot get it if not found")
        void getBookById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/books/get/1000")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertNotNull(errorResponse);
                        assertEquals("NOT FOUND", errorResponse.getTitle());
                    });
        }
    }


}