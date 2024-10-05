package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.DeliveryPerson;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {}