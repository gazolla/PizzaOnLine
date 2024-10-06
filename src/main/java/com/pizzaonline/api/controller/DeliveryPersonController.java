package com.pizzaonline.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.repository.DeliveryPersonRepository;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-persons")
public class DeliveryPersonController {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @PostMapping
    public ResponseEntity<DeliveryPerson> registerDeliveryPerson(@RequestBody DeliveryPerson deliveryPerson) {
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return new ResponseEntity<>(savedDeliveryPerson, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryPerson>> listDeliveryPersons() {
        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findAll();
        return ResponseEntity.ok(deliveryPersons);
    }
}
