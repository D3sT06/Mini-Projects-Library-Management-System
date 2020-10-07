package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.service.BookItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
class BookItemControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Nested
    @DisplayName("When the book item is valid")
    class ValidBookItem {
        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create it")
        void createBookItem() throws Exception {
            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": 1\n" +
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
        void updateBookItem() throws Exception {
            mockMvc
                    .perform(
                            get("/api/book-items/get-by-book/1")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(result -> {
                        BookItem[] items = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem[].class);
                        mockMvc
                                .perform(
                                        put("/api/book-items/update")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content("{\n" +
                                                        "    \"barcode\": \"" + items[0].getBarcode() + "\",\n" +
                                                        "    \"book\": {\n" +
                                                        "        \"id\": 2\n" +
                                                        "    },\n" +
                                                        "    \"rack\": {\n" +
                                                        "        \"id\": 1\n" +
                                                        "    }\n" +
                                                        "}"))
                                .andExpect(status().isOk());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can delete it")
        void deleteBookItemById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/book-items/get-by-book/1")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(result -> {
                        BookItem[] items = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem[].class);
                        mockMvc
                                .perform(
                                        delete("/api/book-items/delete/" + items[0].getBarcode())
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get it by using barcode")
        void getBookItemByBarcode() throws Exception {

            mockMvc
                    .perform(
                            get("/api/book-items/get-by-book/1")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(result -> {
                        BookItem[] items = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem[].class);
                        mockMvc
                                .perform(
                                        get("/api/book-items/get/" + items[0].getBarcode())
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(item -> {
                                    BookItem bookItem = objectMapper.readValue(item.getResponse().getContentAsString(), BookItem.class);
                                    assertNotNull(bookItem);
                                });
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"MEMBER", "LIBRARIAN"})
        @DisplayName("Then user can get it by using book id")
        void getBookItemsByBook() throws Exception {
            mockMvc
                    .perform(
                            get("/api/book-items/get-by-book/1")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        BookItem[] bookItems = objectMapper.readValue(result.getResponse().getContentAsString(), BookItem[].class);
                        assertNotNull(bookItems);
                        assertEquals(2, bookItems.length);
                    });
        }
    }

    @Nested
    @DisplayName("When the book item is invalid")
    class InvalidBookItem {
        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian cannot create it with status")
        void createBookItemWithStatus() throws Exception {
            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"status\": \"AVAILABLE\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": 1\n" +
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
            mockMvc
                    .perform(
                            post("/api/book-items/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"barcode\": \"test\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": 1\n" +
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
            mockMvc
                    .perform(
                            put("/api/book-items/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": 1\n" +
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
            mockMvc
                    .perform(
                            put("/api/book-items/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"barcode\": \"test\",\n" +
                                            "    \"book\": {\n" +
                                            "        \"id\": 1\n" +
                                            "    },\n" +
                                            "    \"rack\": {\n" +
                                            "        \"id\": 1\n" +
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