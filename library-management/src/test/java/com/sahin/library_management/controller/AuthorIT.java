package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.helper.AuthorViewHelper;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Author Endpoints:")
public class AuthorIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("authorLoader")
    protected Loader<?> loader;

    @Nested
    @DisplayName("When the author is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidAuthor {

        @BeforeAll
        void setup() {
            loader.loadDb();
        }

        @AfterAll
        void clear() {
            loader.clearDb();
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then user can find all")
        @Order(1)
        void getAll() throws Exception {

            long expectedResult = loader.getAll().size();

            mockMvc
                    .perform(
                            get("/api/authors/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        AuthorViewHelper[] authors = objectMapper.readValue(result.getResponse().getContentAsString(), AuthorViewHelper[].class);
                        assertEquals(expectedResult, authors.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can find it by id")
        @Order(2)
        void getById() throws Exception {

            AuthorEntity authorEntity = (AuthorEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/authors/get/" + authorEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        AuthorViewHelper author = objectMapper.readValue(result.getResponse().getContentAsString(), AuthorViewHelper.class);
                        assertNotNull(author);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can create it")
        @Order(3)
        void createAuthor() throws Exception {

            mockMvc
                    .perform(
                            post("/api/authors/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"name\": \"İlber\",\n" +
                                            "    \"surname\": \"Ortaylı\"\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Author author = objectMapper.readValue(result.getResponse().getContentAsString(), Author.class);
                        assertNotNull(author);
                        assertNotNull(author.getId());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can update it")
        @Order(4)
        void updateAuthor() throws Exception {

            AuthorEntity authorEntity = (AuthorEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/authors/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + authorEntity.getId() + ",\n" +
                                            "    \"name\": \"Test\",\n" +
                                            "    \"surname\": \"Test\"\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can delete it")
        @Order(5)
        void deleteAuthorById() throws Exception {

            AuthorEntity authorEntity = (AuthorEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            delete("/api/authors/delete/" + authorEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("When the author is invalid")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidAuthor {

        @BeforeAll
        void setup() {
            loader.loadDb();
        }

        @AfterAll
        void clear() {
            loader.clearDb();
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then librarian cannot create it with id")
        void createAuthorWithId() throws Exception {
            mockMvc
                    .perform(
                            post("/api/authors/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 5,\n" +
                                            "    \"name\": \"Test\",\n" +
                                            "    \"surname\": \"Test\"\n" +
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
        void updateAuthorWithoutId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/authors/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"name\": \"Test\",\n" +
                                            "    \"surname\": \"Test\"\n" +
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
        void updateAuthorWithNonExistingId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/authors/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 1000,\n" +
                                            "    \"name\": \"Test\",\n" +
                                            "    \"surname\": \"Test\"\n" +
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
        void deleteAuthorById() throws Exception {

            mockMvc
                    .perform(
                            delete("/api/authors/delete/1000")
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
        void getAuthorById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/authors/get/1000")
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
