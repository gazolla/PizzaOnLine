package com.pizzaonline.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateEmployee() throws Exception {
        mockMvc.perform(post("/api/employees")
                .contentType("application/json")
                .content("{\"name\":\"Alice\",\"position\":\"Manager\",\"login\":\"alice123\",\"password\":\"securepassword\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.position").value("Manager"))
                .andExpect(jsonPath("$.login").value("alice123"));
    }

   
}
