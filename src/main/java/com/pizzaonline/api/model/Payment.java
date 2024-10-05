package com.pizzaonline.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payment {

    public Payment() { }

	public Payment(Long id, Double amount, String paymentMethod, LocalDateTime paymentDate) {
		this.id = id;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.paymentDate = paymentDate;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  //  @OneToOne
  //  @JoinColumn(name = "order_id")
  //  private Order order;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    // Getters
    public Long getId() {
        return id;
    }

   // public Order getOrder() {
   //     return order;
  //  }

    public Double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
}
