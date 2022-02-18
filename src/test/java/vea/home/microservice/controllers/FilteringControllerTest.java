package vea.home.microservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {FilteringController.class})
class FilteringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successFilteringGetTest() throws Exception {

        mockMvc.perform(get("/filtering"))
                .andExpect(jsonPath("$.field1").value("value1"))
                .andExpect(jsonPath("$.field2").value("value2"))
                .andExpect(jsonPath("$.field3").doesNotExist());

    }
}