package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.LibrarianEntity;
import com.sahin.library_management.infra.model.account.Librarian;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Librarian Endpoints:")
class LibrarianIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("librarianLoader")
    protected Loader<?> loader;

    @Nested
    @DisplayName("When the librarian object is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidLibrarian {

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
        @DisplayName("Then a librarian can get all")
        @Order(1)
        void getAll() throws Exception {

            long expectedResult = loader.getAll().size();

            mockMvc
                    .perform(
                            get("/api/librarians/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Librarian[] librarians = objectMapper.readValue(result.getResponse().getContentAsString(), Librarian[].class);
                        assertNotNull(librarians);
                        assertEquals(expectedResult, librarians.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create it")
        @Order(2)
        void createLibrarian() throws Exception {
            mockMvc
                    .perform(
                            post("/api/librarians/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"name\": \"test\",\n" +
                                            "    \"surname\": \"test\"\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can update it")
        @Order(3)
        void updateLibrarian() throws Exception {

            List<LibrarianEntity> entities = (List<LibrarianEntity>) loader.getAll();

            mockMvc
                    .perform(
                            put("/api/librarians/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + entities.get(0).getId() +",\n" +
                                            "    \"name\": \"test\",\n" +
                                            "    \"surname\": \"test\"\n" +
                                            "}"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can delete it by barcode")
        @Order(4)
        void deleteLibrarianByBarcode() throws Exception {

            List<LibrarianEntity> entities = (List<LibrarianEntity>) loader.getAll();

            mockMvc
                    .perform(
                            delete("/api/librarians/delete/" + entities.get(0).getLibraryCard().getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can get it by barcode")
        @Order(5)
        void getLibrarianByBarcode() throws Exception {

            List<LibrarianEntity> entities = (List<LibrarianEntity>) loader.getAll();

            mockMvc
                    .perform(
                            get("/api/librarians/get/" + entities.get(0).getLibraryCard().getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        Librarian librarian = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Librarian.class);
                        assertNotNull(librarian);
                    });
        }
    }
}