package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}