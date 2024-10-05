package com.pizzaonline.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String position;
    private String login;
    private String password;

   // @OneToMany(mappedBy = "responsibleEmployee")
   // private Set<Order> managedOrders;

    // Default constructor (required by Hibernate)
    public Employee() { }

    // Constructor with parameters (optional)
    public Employee(Long id, String name, String position, String login, String password) {//, Set<Order> managedOrders) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.login = login;
        this.password = password;
     //   this.managedOrders = managedOrders;
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

  //  public Set<Order> getManagedOrders() {
  //      return managedOrders;
  //  }
}
