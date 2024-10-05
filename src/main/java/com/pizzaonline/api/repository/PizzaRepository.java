package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {}