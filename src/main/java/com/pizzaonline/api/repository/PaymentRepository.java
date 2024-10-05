package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}