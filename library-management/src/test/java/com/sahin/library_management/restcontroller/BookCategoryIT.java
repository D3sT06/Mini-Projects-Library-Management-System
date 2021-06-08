package com.sahin.library_management.restcontroller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.helper.CategoryViewHelper;
import com.sahin.library_management.infra.model.book.BookCategory;
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
@DisplayName("Category Endpoints:")
public class BookCategoryIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("categoryLoader")
    protected Loader<?> loader;

    @Nested
    @DisplayName("When the category is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidCategory {

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
                            get("/api/categories/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        CategoryViewHelper[] categories = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryViewHelper[].class);
                        assertEquals(expectedResult, categories.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can find it by id")
        @Order(2)
        void getById() throws Exception {

            BookCategoryEntity categoryEntity = (BookCategoryEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/categories/get/" + categoryEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        CategoryViewHelper category = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryViewHelper.class);
                        assertNotNull(category);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can create it")
        @Order(3)
        void createCategory() throws Exception {

            mockMvc
                    .perform(
                            post("/api/categories/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"name\": \"test\"\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        BookCategory category = objectMapper.readValue(result.getResponse().getContentAsString(), BookCategory.class);
                        assertNotNull(category);
                        assertNotNull(category.getId());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can update it")
        @Order(4)
        void updateCategory() throws Exception {

            BookCategoryEntity categoryEntity = (BookCategoryEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/categories/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + categoryEntity.getId() + ",\n" +
                                            "    \"name\": \"test-2\"\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can delete it")
        @Order(5)
        void deleteCategoryById() throws Exception {

            BookCategoryEntity categoryEntity = (BookCategoryEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            delete("/api/categories/delete/" + categoryEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("When the category is invalid")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidCategory {

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
        void createCategoryWithId() throws Exception {
            mockMvc
                    .perform(
                            post("/api/categories/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 5,\n" +
                                            "    \"name\": \"test\"\n" +
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
        void updateCategoryWithoutId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/categories/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"name\": \"test\"\n" +
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
        void updateCategoryWithNonExistingId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/categories/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 1000,\n" +
                                            "    \"name\": \"test\"\n" +
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
        void deleteCategoryById() throws Exception {

            mockMvc
                    .perform(
                            delete("/api/categories/delete/1000")
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
        void getCategoryById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/categories/get/1000")
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