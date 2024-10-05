package com.pizzaonline.api.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToMany
    @JoinTable(
        name = "order_pizza",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private Set<Pizza> pizzas;

    @ManyToOne
    private DeliveryPerson deliveryPerson;

    @ManyToOne
    private Employee responsibleEmployee;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalAmount;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    public Order() {}

    public Order(Long id, Client client, Set<Pizza> pizzas, DeliveryPerson deliveryPerson, Employee responsibleEmployee,
			LocalDateTime orderDate, OrderStatus status, Double totalAmount, Payment payment) {
		super();
		this.id = id;
		this.client = client;
		this.pizzas = pizzas;
		this.deliveryPerson = deliveryPerson;
		this.responsibleEmployee = responsibleEmployee;
		this.orderDate = orderDate;
		this.status = status;
		this.totalAmount = totalAmount;
		this.payment = payment;
	}

    
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	// Getters
    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Set<Pizza> getPizzas() {
        return pizzas;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
    }

    public Employee getResponsibleEmployee() {
        return responsibleEmployee;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Payment getPayment() {
        return payment;
    }
}
