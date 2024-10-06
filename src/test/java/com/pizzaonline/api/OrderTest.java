package com.pizzaonline.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.model.Employee;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.model.Pizza;
import com.pizzaonline.api.repository.ClientRepository;
import com.pizzaonline.api.repository.DeliveryPersonRepository;
import com.pizzaonline.api.repository.EmployeeRepository;
import com.pizzaonline.api.repository.PaymentRepository;
import com.pizzaonline.api.repository.PizzaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    
    private Client client;
    private DeliveryPerson deliveryPerson;
    private Employee employee;
    private Pizza pizza;
    private Payment payment;
    

	private void createAllEntitiesForOrder() {
		 client = new Client(null, "Cliente Teste", "cliente@test.com", "123456789", "Rua A");
         clientRepository.save(client);

         pizza = new Pizza(null, "Pizza Teste", "Descrição da pizza", 25.0);
         pizzaRepository.save(pizza);

         deliveryPerson = new DeliveryPerson(null, "Entregador Teste", "987654321");
         deliveryPersonRepository.save(deliveryPerson);

         employee = new Employee(null, "Funcionário Teste", "Gerente", "funcionario", "senha");
         employeeRepository.save(employee);

         payment = new Payment(null, 100.0, "Credit Card", Timestamp.valueOf("2024-10-06 12:47:47"));
         paymentRepository.save(payment);
	}

	private void cleanAllTables() {
		paymentRepository.deleteAll();
        pizzaRepository.deleteAll();
        deliveryPersonRepository.deleteAll();
        employeeRepository.deleteAll();
        clientRepository.deleteAll();
	}

    @Test
    void shouldCreateOrder() throws Exception {
    	cleanAllTables();
        
        createAllEntitiesForOrder();

    	mockMvc.perform(post("/api/orders")
    	        .contentType("application/json")
    	        .content(String.format("""
    	            {
    	                "client": {"id": %d},
    	                "pizzas": [{"id": %d}],
    	                "deliveryPerson": {"id": %d},
    	                "responsibleEmployee": {"id": %d},
    	                "orderDate": "2024-10-06 12:47:47",
    	                "status": "RECEIVED",
    	                "totalAmount": 19.99,
    	                "payment": {"id": %d}
    	            }
    	        """, client.getId(), pizza.getId(), deliveryPerson.getId(), employee.getId(), payment.getId())))
    	        .andExpect(status().isCreated())
    	        .andExpect(jsonPath("$.status").value("RECEIVED"))
    	        .andExpect(jsonPath("$.totalAmount").value(19.99));

    }
    
    @Test
    void shouldCreateOrderWihtoutPayment() throws Exception {

	    Client c = clientRepository.save(new Client(null, "Cliente Teste", "cliente@test.com", "123456789", "Rua A"));
	    Pizza p = pizzaRepository.save(new Pizza(null, "Pizza Teste", "Descrição da pizza", 25.0));
	    DeliveryPerson dp = deliveryPersonRepository.save(new DeliveryPerson(null, "Entregador Teste", "987654321"));
	    Employee e = employeeRepository.save(new Employee(null, "Funcionário Teste", "Gerente", "funcionario", "senha"));

	    
    	mockMvc.perform(post("/api/orders")
    	        .contentType("application/json")
    	        .content(String.format("""
    	            {
    	                "client": {"id": %d},
    	                "pizzas": [{"id": %d}],
    	                "deliveryPerson": {"id": %d},
    	                "responsibleEmployee": {"id": %d},
    	                "orderDate": "2024-10-06 12:47:47",
    	                "status": "RECEIVED",
    	                "totalAmount": 19.99
    	            }
    	        """, c.getId(), p.getId(), dp.getId(), e.getId())))
    	        .andExpect(status().isCreated())
    	        .andExpect(jsonPath("$.status").value("RECEIVED"))
    	        .andExpect(jsonPath("$.totalAmount").value(19.99));

    }


    
}
