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
public class DeliveryPersonTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateDeliveryPerson() throws Exception {
        mockMvc.perform(post("/api/delivery-persons")
                .contentType("application/json")
                .content("{\"name\":\"Jane Doe\",\"phone\":\"987654321\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.phone").value("987654321"));
    }

}
