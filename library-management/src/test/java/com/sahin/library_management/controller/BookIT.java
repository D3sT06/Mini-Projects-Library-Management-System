package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.entity_model.BookEntity;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.repository.AuthorRepository;
import com.sahin.library_management.repository.BookCategoryRepository;
import com.sahin.library_management.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Book Endpoints:")
class BookIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("bookLoader")
    protected Loader<?> bookLoader;

    @Autowired
    @Qualifier("categoryLoader")
    protected Loader<?> categoryLoader;

    @Autowired
    @Qualifier("authorLoader")
    protected Loader<?> authorLoader;

    @Nested
    @DisplayName("When the book is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidBook {

        @BeforeAll
        void setup() {
            authorLoader.loadDb();
            categoryLoader.loadDb();
            bookLoader.loadDb();
        }

        @AfterAll
        void clear() {
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can find all the books without using any filter")
        @Order(1)
        void searchBooksWithNoFilter() throws Exception {

            long expectedResult = bookLoader.getAll().size();

            mockMvc
                    .perform(
                            post("/api/books/search")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book[] books = objectMapper.readValue(result.getResponse().getContentAsString(), Book[].class);
                        assertEquals(expectedResult, books.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can find the book he wants by using filters")
        @Order(2)
        void searchBooksWithFilter() throws Exception {

            long expectedResult = ((List<BookEntity>) bookLoader.getAll()).stream()
                    .filter(entity -> entity.getTitle().contains("19"))
                    .filter(entity -> entity.getCategory().getId().equals(1L))
                    .filter(entity -> entity.getAuthor().getId().equals(3L))
                    .count();

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
                        assertEquals(expectedResult, books.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create a book")
        @Order(3)
        void createBook() throws Exception {

            BookCategoryEntity category = (BookCategoryEntity) categoryLoader.getAll().get(0);
            AuthorEntity author = (AuthorEntity) authorLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/books/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"title\": \"Boğulmamak İçin\",\n" +
                                            "    \"publicationDate\": \"12/10/1990\",\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": " + category.getId() + "\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": " + author.getId() + "\n" +
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
        @Order(4)
        void updateBook() throws Exception {

            BookEntity bookEntity = (BookEntity) bookLoader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/books/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + bookEntity.getId() + ",\n" +
                                            "    \"title\": \"Test\",\n" +
                                            "    \"publicationDate\": \"12/10/1990\",\n" +
                                            "    \"category\": {\n" +
                                            "        \"id\": " + bookEntity.getCategory().getId() + "\n" +
                                            "    },\n" +
                                            "    \"author\": {\n" +
                                            "        \"id\": " + bookEntity.getAuthor().getId() + "\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can delete a book")
        @Order(5)
        void deleteBookById() throws Exception {

            BookEntity bookEntity = (BookEntity) bookLoader.getAll().get(0);

            mockMvc
                    .perform(
                            delete("/api/books/delete/" + bookEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get a book")
        @Order(6)
        void getBookById() throws Exception {
            BookEntity bookEntity = (BookEntity) bookLoader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/books/get/" + bookEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Book book = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
                        assertNotNull(book);
                    });
        }


    }

    @Nested
    @DisplayName("When the book is invalid")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidBook {

        @BeforeAll
        void setup() {
            authorLoader.loadDb();
            categoryLoader.loadDb();
            bookLoader.loadDb();
        }

        @AfterAll
        void clear() {
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

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
                                            "    \"publicationDate\": \"12/10/1990\",\n" +
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
                                            "    \"publicationDate\": \"12/10/1990\",\n" +
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
                                            "    \"publicationDate\": \"12/10/1990\",\n" +
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