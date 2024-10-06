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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.model.OrderStatus;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.model.Pizza;
import com.pizzaonline.api.repository.ClientRepository;
import com.pizzaonline.api.repository.DeliveryPersonRepository;
import com.pizzaonline.api.repository.OrderRepository;
import com.pizzaonline.api.repository.PaymentRepository;
import com.pizzaonline.api.repository.PizzaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class PaymentTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        // Limpar os dados do banco de dados
        clientRepository.deleteAll();
        pizzaRepository.deleteAll();
        deliveryPersonRepository.deleteAll();
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
        
        // Verificar contagem
        System.out.println("Orders count: " + orderRepository.count());
        System.out.println("Payments count: " + paymentRepository.count());
        System.out.println("Delivery Persons count: " + deliveryPersonRepository.count());
        System.out.println("Pizzas count: " + pizzaRepository.count());
        System.out.println("Clients count: " + clientRepository.count());
    }
    
    
    @Test
    public void testPaymentSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Payment payment = new Payment(null, 100.00, "Credit Card", Timestamp.valueOf(LocalDateTime.now()));
        String json = objectMapper.writeValueAsString(payment);
        System.out.println(json);
    }


    @Test
    public void shouldRegisterPayment() throws Exception {
    	 ObjectMapper objectMapper = new ObjectMapper();
        // Criar e salvar um novo cliente
        Client client = new Client(null, "Test Client", "client@test.com", "123456789", "123 Test St, Test City");
        ResponseEntity<Client> clientResponse = restTemplate.postForEntity("/api/clients", client, Client.class);
        assertEquals(HttpStatus.CREATED, clientResponse.getStatusCode());
        Client rClient = clientResponse.getBody();
        Client c = clientRepository.findById(rClient.getId()).get();

        // Criar e salvar uma nova pizza
        Pizza pizza = new Pizza(null, "Margherita", "Classic Italian pizza", 19.99);
        ResponseEntity<Pizza> pizzaResponse = restTemplate.postForEntity("/api/pizzas", pizza, Pizza.class);
        assertEquals(HttpStatus.CREATED, pizzaResponse.getStatusCode());
        Pizza rPizza = pizzaResponse.getBody();
        Pizza p = pizzaRepository.findById(rPizza.getId()).get();
        
     // Criação de um novo DeliveryPerson
        DeliveryPerson deliveryPerson = new DeliveryPerson(null, "John Doe", "123456789");
        ResponseEntity<DeliveryPerson> DPResponse = restTemplate.postForEntity("/api/delivery-persons", deliveryPerson, DeliveryPerson.class);
        assertEquals(HttpStatus.CREATED, DPResponse.getStatusCode());
        DeliveryPerson rDeliveryPerson = DPResponse.getBody();
        DeliveryPerson dp = deliveryPersonRepository.findById(rDeliveryPerson.getId()).get();
        
        // Criar um pagamento e associá-lo ao pedido
        Payment payment = new Payment(null, 100.00, "Credit Card", Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("/api/payments", payment, Payment.class);
        assertEquals(HttpStatus.CREATED, paymentResponse.getStatusCode());
        Payment rpayment = paymentResponse.getBody();
        Payment pay = paymentRepository.findById(rpayment.getId()).get();
        
     // Criar um novo pedido
        Order order = new Order(
                null, 
                c, 
                new HashSet<>(List.of(p)), 
                dp, 
                null, 
                Timestamp.valueOf(LocalDateTime.now()), 
                OrderStatus.RECEIVED, 
                19.99, 
                pay
            );

        Order savedOrder = orderRepository.save(order);

        // Verificar se o pagamento foi registrado corretamente
      //  ResponseEntity<Order> getOrderResponse = restTemplate.getForEntity("/api/orders/" + savedOrder.getId(), Order.class);
        Order o = orderRepository.findById(savedOrder.getId()).get();
        assertNotNull(o);
        assertNotNull(o.getId());
        String json = objectMapper.writeValueAsString(o);
        System.out.println(json);
      
    }

    @Test
    void shouldListPayments() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/payments", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
