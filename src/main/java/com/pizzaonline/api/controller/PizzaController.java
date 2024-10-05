package com.pizzaonline.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pizzaonline.api.model.Pizza;
import com.pizzaonline.api.repository.PizzaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @PostMapping
    public ResponseEntity<Pizza> registerPizza(@RequestBody Pizza pizza) {
        Pizza savedPizza = pizzaRepository.save(pizza);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPizza);
    }

    @GetMapping
    public ResponseEntity<List<Pizza>> listPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        return ResponseEntity.ok(pizzas);
    }
}
