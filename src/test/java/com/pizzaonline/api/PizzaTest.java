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
public class PizzaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreatePizza() throws Exception {
        mockMvc.perform(post("/api/pizzas")
                .contentType("application/json")
                .content("{\"name\":\"Pepperoni\",\"description\":\"Pepperoni pizza with cheese\",\"price\":25.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pepperoni"))
                .andExpect(jsonPath("$.price").value(25.0));
    }

}
