package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.model.Employee;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.model.OrderStatus;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.model.Pizza;
import com.pizzaonline.api.repository.ClientRepository;
import com.pizzaonline.api.repository.DeliveryPersonRepository;
import com.pizzaonline.api.repository.EmployeeRepository;
import com.pizzaonline.api.repository.OrderRepository;
import com.pizzaonline.api.repository.PaymentRepository;
import com.pizzaonline.api.repository.PizzaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PizzaOrderFlowTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private Client client;
    private DeliveryPerson deliveryPerson;
    private Employee employee;
    private Pizza pizza;
    private Payment payment;
    private Order order;

    @BeforeEach
    @Transactional
    public void setUp() {
        // Limpa os repositórios antes de cada teste
        clientRepository.deleteAll();
        deliveryPersonRepository.deleteAll();
        employeeRepository.deleteAll();
        pizzaRepository.deleteAll();
        orderRepository.deleteAll();
        paymentRepository.deleteAll();

        // Inicializando os dados de teste
        client = new Client(null, "Cliente Teste", "cliente@test.com", "123456789", "Rua A");
        deliveryPerson = new DeliveryPerson(null, "Entregador Teste", "987654321");
        employee = new Employee(null, "Funcionário Teste", "Gerente", "funcionario", "senha");
        pizza = new Pizza(null, "Pizza Teste", "Descrição da pizza", 25.0);
        payment = new Payment(null, 100.00, "Credit Card", Timestamp.valueOf(LocalDateTime.now()));
        
        Client savedClient = clientRepository.save(client);
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        Employee savedEmployee = employeeRepository.save(employee);
        Pizza savedPizza = pizzaRepository.save(pizza);
        Payment savedPayment = paymentRepository.save(payment);
        Payment existingPayment = paymentRepository.findById(payment.getId()).get();
        order = new Order(null, savedClient, new HashSet<>(List.of(savedPizza)), savedDeliveryPerson, savedEmployee, Timestamp.valueOf(LocalDateTime.now()), OrderStatus.RECEIVED, 19.99,  existingPayment);
    }

    @Test
    @Transactional
    public void testPizzaOrderFlow() throws Exception {
        // O cliente faz o pedido de uma pizza
    	ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println(">>>>" + json);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
     
     // Enviar a requisição POST
        ResponseEntity<Order> response = restTemplate.exchange(
        		"/api/orders", 
                HttpMethod.POST, 
                requestEntity, 
                Order.class
        );
        
     // Verificações
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Order savedOrder = response.getBody();
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        // LOG: Informações sobre o pedido após cadastro
        System.out.println("Pedido cadastrado: ID = " + response.getBody().getId() + ", Status = " + response.getBody().getStatus());

        // O funcionário coloca o status da ordem em produção (COOKING)
        // Passo extra para garantir que estamos enviando o pedido correto
        assertNotNull(response.getBody().getId(), "O ID do pedido não deve ser nulo antes de alterar o status para COOKING.");
        
        HttpEntity<Order> cookingRequest = new HttpEntity<>(response.getBody());
        
        // Verificação extra de status antes da mudança para COOKING
        System.out.println("Status do pedido antes da mudança para COOKING: " + response.getBody().getStatus());

        ResponseEntity<Order> cookingResponse = restTemplate.exchange("/api/orders/" + response.getBody().getId() + "/cooking", HttpMethod.PUT, cookingRequest, Order.class);
        
        // LOG: Status da resposta da requisição de mudança de status
        System.out.println("Status HTTP da resposta para mudança para COOKING: " + cookingResponse.getStatusCode());
        System.out.println("Corpo da resposta para mudança para COOKING: " + cookingResponse.getBody());
        System.out.println("Status do pedido depois da mudança para COOKING: " + response.getBody().getStatus());

        // Verificações para garantir que o status foi atualizado corretamente
        assertEquals(HttpStatus.OK, cookingResponse.getStatusCode(), "A resposta da requisição de mudança para COOKING deve ser 200 OK.");
        assertNotNull(cookingResponse.getBody(), "O corpo da resposta não deve ser nulo após a mudança para COOKING.");
        assertEquals(OrderStatus.COOKING, cookingResponse.getBody().getStatus(), "O status do pedido deve ser COOKING após a atualização.");

        // LOG: Status do pedido após a atualização para COOKING
        System.out.println("Status do pedido após mudança para COOKING: " + cookingResponse.getBody().getStatus());

        // O funcionário coloca o status da ordem em pronta para entrega
        ResponseEntity<Order> readyResponse = restTemplate.exchange("/api/orders/" + response.getBody().getId() + "/ready", HttpMethod.PUT, cookingRequest, Order.class);
        assertEquals(HttpStatus.OK, readyResponse.getStatusCode());
        assertEquals(OrderStatus.READY_FOR_DELIVERY, readyResponse.getBody().getStatus());

        // O funcionário coloca o status da ordem como saiu para entrega
        ResponseEntity<Order> outForDeliveryResponse = restTemplate.exchange("/api/orders/" + response.getBody().getId() + "/out-for-delivery", HttpMethod.PUT, cookingRequest, Order.class);
        assertEquals(HttpStatus.OK, outForDeliveryResponse.getStatusCode());
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, outForDeliveryResponse.getBody().getStatus());

        // O entregador coloca como entregue
        ResponseEntity<Order> deliveredResponse = restTemplate.exchange("/api/orders/" + response.getBody().getId() + "/delivered", HttpMethod.PUT, cookingRequest, Order.class);
        assertEquals(HttpStatus.OK, deliveredResponse.getStatusCode());
        assertEquals(OrderStatus.DELIVERED, deliveredResponse.getBody().getStatus());

        // Registra o pagamento da ordem
        Payment payment = new Payment(null, 25.0, "Cartão de Crédito", Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("/api/payments", payment, Payment.class);
        assertEquals(HttpStatus.CREATED, paymentResponse.getStatusCode());
        assertNotNull(paymentResponse.getBody());
        assertEquals(25.0, paymentResponse.getBody().getAmount());
    }
}
