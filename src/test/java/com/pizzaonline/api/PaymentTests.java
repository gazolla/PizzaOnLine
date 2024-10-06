package com.pizzaonline.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.pizzaonline.api.model.Payment;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentTests {

    @Autowired
    private MockMvc mockMvc;

    private Payment testPayment;

    @BeforeEach
    public void setUp() {
        testPayment = new Payment(null, 100.0, "Credit Card", Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void shouldCreatePayment() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType("application/json")
                .content("{\"amount\":100.0,\"paymentMethod\":\"Credit Card\",\"paymentDate\":\"2024-10-06 12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.paymentMethod").value("Credit Card"))
                .andExpect(jsonPath("$.paymentDate").value("2024-10-06 12:00:00"));
    }

    @Test
    void shouldReturnErrorForInvalidPaymentMethod() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType("application/json")
                .content("valor"))
                .andExpect(status().isBadRequest());
    }

}
