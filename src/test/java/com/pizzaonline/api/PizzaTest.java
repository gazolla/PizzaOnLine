package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pizzaonline.api.model.Pizza;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PizzaTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreatePizza() {
        // Criando uma nova pizza com um Set vazio para orders
        Pizza newPizza = new Pizza(null, "Margherita", "Classic Italian pizza", 19.99);
        ResponseEntity<Pizza> pizzaResponse = restTemplate.postForEntity("/api/pizzas", newPizza, Pizza.class);
        
        assertEquals(HttpStatus.CREATED, pizzaResponse.getStatusCode());
        Pizza registeredPizza = pizzaResponse.getBody();
        assertNotNull(registeredPizza.getId());
    }

    @Test
    void shouldListPizzas() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/pizzas", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
