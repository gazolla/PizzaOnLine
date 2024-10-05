package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.repository.PaymentRepository;

@SpringBootTest
class PaymentRepositoryTests {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void shouldCreateAndFindPayment() {
        Payment newPayment = new Payment(null, 49.99, "Credit Card", LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(newPayment);

        assertNotNull(savedPayment);
        assertNotNull(savedPayment.getId());
        assertEquals("Credit Card", savedPayment.getPaymentMethod());

        Payment foundPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);
        assertNotNull(foundPayment);
        assertEquals(savedPayment.getId(), foundPayment.getId());
    }

}
