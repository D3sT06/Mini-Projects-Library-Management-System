package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.repository.LibrarianRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LibraryManagementApp.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Librarian Endpoints:")
@Disabled
class LibrarianControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected LibrarianRepository librarianRepository;

    @Nested
    @DisplayName("When the librarian is valid")
    class ValidLibrarian {

        @AfterEach
        void afterEach() {
            //System.out.println(librarianRepository.count());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create it")
        @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
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
        void updateLibrarian() throws Exception {

            List<LibrarianEntity> entities = librarianRepository.findAll();

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
        @Rollback
        void deleteLibrarianByBarcode() throws Exception {

            List<LibrarianEntity> entities = librarianRepository.findAll();

            mockMvc
                    .perform(
                            delete("/api/librarians/delete/" + entities.get(0).getLibraryCard().getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can get it by barcode")
        void getLibrarianByBarcode() throws Exception {

            List<LibrarianEntity> entities = librarianRepository.findAll();

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

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can get all")
        void getAll() throws Exception {
            mockMvc
                    .perform(
                            get("/api/librarians/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Librarian[] librarians = objectMapper.readValue(result.getResponse().getContentAsString(), Librarian[].class);
                        assertNotNull(librarians);
                        assertEquals(librarianRepository.count(), librarians.length);
                    });
        }
    }
}