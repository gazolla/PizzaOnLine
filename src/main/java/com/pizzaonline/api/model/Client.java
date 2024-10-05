package com.pizzaonline.api.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    
    @OneToMany(mappedBy = "client")
    Set<Order> orders;

    public Client() {}

    public Client(Long id, String nome, String email, String telefone, String endereco, Set<Order> orders) {
        this.id = id;
        this.name = nome;
        this.email = email;
        this.phone = telefone;
        this.address = endereco;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
    
    public Set<Order> getOrders(){
    	return orders;
    }


}



