package vea.home.microservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {PersonVersioningController.class})
class PersonVersioningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPersonV1() throws Exception {
        mockMvc.perform(get("/v1/person").with(httpBasic("testUser","testUserPassword")))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Eugen Volkoedov"));
    }

    @Test
    void getPersonV2() throws Exception {
        mockMvc.perform(get("/v2/person").with(httpBasic("testUser","testUserPassword")))
                .andDo(print())
                .andExpect(jsonPath("$.name.firstName").value("Eugen"))
                .andExpect(jsonPath("$.name.lastName").value("Volkoedov"));
    }
}