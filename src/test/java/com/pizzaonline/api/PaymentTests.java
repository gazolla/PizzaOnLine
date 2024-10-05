package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pizzaonline.api.model.Payment;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterPayment() {
        Payment newPayment = new Payment(null, 100.00, "Credit Card", LocalDateTime.now());
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("/api/pagaments", newPayment, Payment.class);
        
        assertEquals(HttpStatus.CREATED, paymentResponse.getStatusCode());
        Payment registeredPayment = paymentResponse.getBody();
        assertNotNull(registeredPayment.getId());
        assertEquals(newPayment.getAmount(), registeredPayment.getAmount());
        assertEquals(newPayment.getPaymentMethod(), registeredPayment.getPaymentMethod());
    }

    @Test
    void shouldListPayments() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/pagaments", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
