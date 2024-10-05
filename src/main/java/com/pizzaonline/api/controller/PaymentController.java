package com.pizzaonline.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.repository.PaymentRepository;

@RestController
@RequestMapping("/api/pagamentos")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping
    public Payment registrarPagamento(@RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }
}