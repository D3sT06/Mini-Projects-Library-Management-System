package com.sahin.library_management.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.jpa.RackEntity;
import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.model.book.Rack;
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
@DisplayName("Rack Endpoints:")
public class RackIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("rackLoader")
    protected Loader<?> loader;

    @Nested
    @DisplayName("When the rack is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidRack {

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
                            get("/api/racks/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Rack[] racks = objectMapper.readValue(result.getResponse().getContentAsString(), Rack[].class);
                        assertEquals(expectedResult, racks.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can find it by id")
        @Order(2)
        void getById() throws Exception {

            RackEntity rackEntity = (RackEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            get("/api/racks/get/" + rackEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Rack rack = objectMapper.readValue(result.getResponse().getContentAsString(), Rack.class);
                        assertNotNull(rack);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can create it")
        @Order(3)
        void createRack() throws Exception {

            mockMvc
                    .perform(
                            post("/api/racks/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"location\": \"B-3\"\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Rack rack = objectMapper.readValue(result.getResponse().getContentAsString(), Rack.class);
                        assertNotNull(rack);
                        assertNotNull(rack.getId());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can update it")
        @Order(4)
        void updateRack() throws Exception {

            RackEntity rackEntity = (RackEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            put("/api/racks/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": " + rackEntity.getId() + ",\n" +
                                            "    \"location\": \"Test\"\n" +
                                            "}"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Rack rack = objectMapper.readValue(result.getResponse().getContentAsString(), Rack.class);
                        assertNotNull(rack);
                        assertEquals("Test", rack.getLocation());
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = "LIBRARIAN")
        @DisplayName("Then librarian can delete it")
        @Order(5)
        void deleteRackById() throws Exception {

            RackEntity rackEntity = (RackEntity) loader.getAll().get(0);

            mockMvc
                    .perform(
                            delete("/api/racks/delete/" + rackEntity.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("When the rack is invalid")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidRack {

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
        void createRackWithId() throws Exception {
            mockMvc
                    .perform(
                            post("/api/racks/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 5,\n" +
                                            "    \"location\": \"test\"\n" +
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
        void updateRackWithoutId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/racks/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"location\": \"Test\"\n" +
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
        void updateRackWithNonExistingId() throws Exception {
            mockMvc
                    .perform(
                            put("/api/racks/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\n" +
                                            "    \"id\": 1000,\n" +
                                            "    \"location\": \"Test\"\n" +
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
        void deleteRackById() throws Exception {

            mockMvc
                    .perform(
                            delete("/api/racks/delete/1000")
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
        void getRackById() throws Exception {

            mockMvc
                    .perform(
                            get("/api/racks/get/1000")
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
