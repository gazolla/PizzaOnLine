package com.pizzaonline.api.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;

    @ManyToMany(mappedBy = "pizzas")
    private Set<Order> orders;

    // Construtor
    public Pizza() {}

    public Pizza(Long id, String name, String description, Double price, Set<Order> orders) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orders = orders;
    }

    // Métodos get
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Set<Order> getOrders() {
        return orders;
    }
}
