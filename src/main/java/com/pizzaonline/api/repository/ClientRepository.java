package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {}
