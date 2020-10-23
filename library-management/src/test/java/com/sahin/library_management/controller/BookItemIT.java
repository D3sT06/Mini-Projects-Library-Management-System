package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.model.book.BookItem;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Book Item Endpoints:")
class BookItemIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

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

    @Nested
    @DisplayName("When the book item is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidBookItem {

        @BeforeAll
        void setup() {
            authorLoader.loadDb();
            categoryLoader.loadDb();
            bookLoader.loadDb();
            rackLoader.loadDb();
            itemLoader.loadDb();
        }

        @AfterAll
        void clear() {
            itemLoader.clearDb();
            rackLoader.clearDb();
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get it by using barcode")
        @Order(1)
        void getBookItemByBarcode() throws Exception {

            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/book-items/get/" + itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(item -> {
                        BookItem bookItem = objectMapper.readValue(item.getResponse().getContentAsString(), BookItem.class);
                        assertNotNull(bookItem);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get it by using book id")
        @Order(2)
        void getBookItemsByBook() throws Exception {
            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/book-items/get-by-book/" + itemEntity.getBook().getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        BookItem[] bookItems = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem[].class);
                        assertNotNull(bookItems);
                        assertEquals(2, bookItems.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create it")
        @Order(3)
        void createBookItem() throws Exception {

            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        BookItem item = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem.class);
                        assertNotNull(item);
                        assertNotNull(item.getBarcode());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can update it")
        @Order(4)
        void updateBookItem() throws Exception {

            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);
            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/book-items/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"barcode\": \"" + itemEntity.getBarcode() + "\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
                                            "    }\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can delete it")
        @Order(5)
        void deleteBookItemById() throws Exception {

            BookItemEntity itemEntity = (BookItemEntity) itemLoader.getAll().get(0);

            mockMvc
                    .perform(
                            delete("/api/book-items/delete/" + itemEntity.getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("When the book item is invalid")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidBookItem {

        @BeforeAll
        void setup() {
            authorLoader.loadDb();
            categoryLoader.loadDb();
            bookLoader.loadDb();
            rackLoader.loadDb();
            itemLoader.loadDb();
        }

        @AfterAll
        void clear() {
            itemLoader.clearDb();
            rackLoader.clearDb();
            bookLoader.clearDb();
            authorLoader.clearDb();
            categoryLoader.clearDb();
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot create it with status")
        void createBookItemWithStatus() throws Exception {

            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"status\": \"AVAILABLE\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
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
        @DisplayName("Then a librarian cannot create it with barcode")
        void createBookItemWithBarcode() throws Exception {

            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"barcode\": \"test\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
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
        @DisplayName("Then a librarian cannot update it without barcode")
        void updateBookItemWithoutBarcode() throws Exception {

            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/book-items/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
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
        @DisplayName("Then a librarian cannot create it with non existing barcode")
        void updateBookItemWithInvalidBarcode() throws Exception {

            BookEntity book = (BookEntity) bookLoader.getAll().get(0);
            RackEntity rack = (RackEntity) rackLoader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/book-items/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"barcode\": \"test\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": " + book.getId() + "\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": " + rack.getId() + "\n" +
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
                            delete("/api/book-items/delete/test")
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
        void getBookItemById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/book-items/get/1000")
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