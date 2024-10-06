package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.model.OrderStatus;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.model.Pizza;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {

	@Autowired
    private TestRestTemplate restTemplate;

	 
    @Test
    public void testOrderSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
     // Criar e salvar um novo cliente
        Client client = new Client(null, "Test Client", "client@test.com", "123456789", "123 Test St, Test City");
        ResponseEntity<Client> clientResponse = restTemplate.postForEntity("/api/clients", client, Client.class);
        assertEquals(HttpStatus.CREATED, clientResponse.getStatusCode());
        Client rClient = clientResponse.getBody();

        // Criar e salvar uma nova pizza
        Pizza pizza = new Pizza(null, "Margherita", "Classic Italian pizza", 19.99);
        ResponseEntity<Pizza> pizzaResponse = restTemplate.postForEntity("/api/pizzas", pizza, Pizza.class);
        assertEquals(HttpStatus.CREATED, pizzaResponse.getStatusCode());
        Pizza rPizza = pizzaResponse.getBody();
        
     // Criação de um novo DeliveryPerson
        DeliveryPerson deliveryPerson = new DeliveryPerson(null, "John Doe", "123456789");
        ResponseEntity<DeliveryPerson> DPResponse = restTemplate.postForEntity("/api/delivery-persons", deliveryPerson, DeliveryPerson.class);
        assertEquals(HttpStatus.CREATED, DPResponse.getStatusCode());
        DeliveryPerson rDeliveryPerson = DPResponse.getBody();
        
        // Criar um pagamento e associá-lo ao pedido
        Payment payment = new Payment(null, 100.00, "Credit Card", Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("/api/payments", payment, Payment.class);
        assertEquals(HttpStatus.CREATED, paymentResponse.getStatusCode());
        Payment rpayment = paymentResponse.getBody();
        
     // Criar um novo pedido
        Order order = new Order(
                null, 
                rClient, 
                new HashSet<>(List.of(rPizza)), 
                rDeliveryPerson, 
                null, 
                Timestamp.valueOf(LocalDateTime.now()), 
                OrderStatus.RECEIVED, 
                19.99, 
                rpayment
            );

        String json = objectMapper.writeValueAsString(order);
        System.out.println(json);
    }

}
