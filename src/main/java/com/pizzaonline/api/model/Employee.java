package com.pizzaonline.api.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String position;
    private String login;
    private String password;


    public Employee() { }

    // Constructor with parameters (optional)
    public Employee(Long id, String name, String position, String login, String password) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.login = login;
        this.password = password;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
