package com.sahin.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.LibraryManagementApp;
import com.sahin.library_management.bootstrap.Loader;
import com.sahin.library_management.infra.entity.MemberEntity;
import com.sahin.library_management.infra.model.account.Member;
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
@DisplayName("Member Endpoints:")
class MemberIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("memberLoader")
    protected Loader<?> loader;

    @Nested
    @DisplayName("When the member object is valid")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidMember {

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
                            get("/api/members/getAll")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Member[] members = objectMapper.readValue(result.getResponse().getContentAsString(), Member[].class);
                        assertNotNull(members);
                        assertEquals(expectedResult, members.length);
                    });
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can create it")
        @Order(2)
        void createMember() throws Exception {
            mockMvc
                    .perform(
                            post("/api/members/create")
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
        void updateMember() throws Exception {

            List<MemberEntity> entities = (List<MemberEntity>) loader.getAll();

            mockMvc
                    .perform(
                            put("/api/members/update")
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
        void deleteMemberByBarcode() throws Exception {

            List<MemberEntity> entities = (List<MemberEntity>) loader.getAll();

            mockMvc
                    .perform(
                            delete("/api/members/delete/" + entities.get(0).getLibraryCard().getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "${UUID.randomUUID().toString()}", roles = {"LIBRARIAN"})
        @DisplayName("Then a librarian can get it by barcode")
        @Order(5)
        void getMemberByBarcode() throws Exception {

            List<MemberEntity> entities = (List<MemberEntity>) loader.getAll();

            mockMvc
                    .perform(
                            get("/api/members/get/" + entities.get(0).getLibraryCard().getBarcode())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        Member member = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Member.class);
                        assertNotNull(member);
                    });
        }
    }

}