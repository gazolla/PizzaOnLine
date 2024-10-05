package com.pizzaonline.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DeliveryPerson {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String name;   
    private String phone;
    
   // @OneToMany(mappedBy = "deliveryPerson")
    //private Set<Order> deliveredOrders;

    // Default constructor (required by Hibernate)
    public DeliveryPerson() { }

    // Constructor with all parameters
    public DeliveryPerson(Long id, String name, String phone){//, Set<Order> deliveredOrders) {
        this.id = id;
        this.name = name;
        this.phone = phone;
       // this.deliveredOrders = deliveredOrders;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

   // public Set<Order> getDeliveredOrders() {
   //     return deliveredOrders;
   // }
}
