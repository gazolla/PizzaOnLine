package com.pizzaonline.api.model;

import java.sql.Timestamp;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties("orders")
    private Client client;
    
    @ManyToMany
    @JoinTable(
        name = "order_pizza",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    @JsonIgnoreProperties("orders")
    private Set<Pizza> pizzas;
    
    @ManyToOne
    @JoinColumn(name = "deliveryperson_id")
    @JsonIgnoreProperties("orders")
    private DeliveryPerson deliveryPerson;
    
    @ManyToOne
    @JoinColumn(name = "responsibleemployee_id")
    @JsonIgnoreProperties("orders")
    private Employee responsibleEmployee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp orderDate;

    private String status;

    private Double totalAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    @JsonIgnoreProperties("orders")
    private Payment payment;

    public Order() {}

    public Order(Long id, Client client, Set<Pizza> pizzas, DeliveryPerson deliveryPerson, Employee responsibleEmployee,
    		Timestamp orderDate, String status, Double totalAmount, Payment payment) {
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

    
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Payment getPayment() {
        return payment;
    }

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
}
