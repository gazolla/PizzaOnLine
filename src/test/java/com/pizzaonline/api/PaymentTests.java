package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.model.Pizza;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterPayment() {
        // Criar e salvar um novo Client antes de registrar o pagamento
        Client newClient = new Client(null, "Test Client", "client@test.com", "123456789", "123 Test St, Test City", new HashSet<Order>());
        ResponseEntity<Client> clientResponse = restTemplate.postForEntity("/api/clients", newClient, Client.class);
        assertEquals(HttpStatus.CREATED, clientResponse.getStatusCode());
        Client registeredClient = clientResponse.getBody();
        assertNotNull(registeredClient.getId(), "O ID do cliente deve ser gerado");

        // Criar um pedido com esse cliente
        Order newOrder = createTestOrder(registeredClient);
        ResponseEntity<Order> orderResponse = restTemplate.postForEntity("/api/orders", newOrder, Order.class);
        assertEquals(HttpStatus.CREATED, orderResponse.getStatusCode());
        Order registeredOrder = orderResponse.getBody();
        assertNotNull(registeredOrder.getId(), "O ID do pedido deve ser gerado");

        // Criar um pagamento associado ao pedido
        Payment newPayment = new Payment(null, registeredOrder, 100.00, "Credit Card", LocalDateTime.now());
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("/api/pagaments", newPayment, Payment.class);

        // Verificar o status da resposta e validar os dados
        assertEquals(HttpStatus.CREATED, paymentResponse.getStatusCode(), "O pagamento deve ser criado com sucesso");
        Payment registeredPayment = paymentResponse.getBody();
        assertNotNull(registeredPayment.getId(), "O ID do pagamento deve ser gerado");
        assertEquals(newPayment.getAmount(), registeredPayment.getAmount(), "O valor do pagamento deve corresponder ao esperado");
        assertEquals(newPayment.getPaymentMethod(), registeredPayment.getPaymentMethod(), "O método de pagamento deve ser igual ao esperado");
        assertEquals(registeredOrder.getId(), registeredPayment.getOrder().getId(), "O pedido associado deve ser o mesmo");
    }

    private Order createTestOrder(Client client) {
        // Criar uma nova pizza para o pedido
        Pizza pizza = new Pizza(null, "Margherita", "Classic Italian pizza", 19.99, new HashSet<>());
        HashSet<Pizza> pizzas = new HashSet<>();
        pizzas.add(pizza);

        // Criar um novo pedido
        return new Order(null, client, pizzas, null, null, LocalDateTime.now(), null, 19.99, null);
    }

    @Test
    void shouldListPayments() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/pagaments", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


}
