package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
import com.pizzaonline.api.repository.PaymentRepository;
import com.pizzaonline.api.repository.PizzaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PizzaOrderFlowTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private DeliveryPersonRepository deliveryPersonRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	private Client client;
	private DeliveryPerson deliveryPerson;
	private Employee employee;
	private Pizza pizza;

	@Test
	@Transactional
	public void testPizzaOrderFlow() throws Exception {
		cleanAllTables();

		createAllEntitiesForOrder();

		Order createdOrder = createOrderWithoutPayment();
		
		System.out.println("created Order:" + createdOrder.getId());
		
		   MvcResult orderResult = mockMvc.perform(get("/api/orders/" + createdOrder.getId()))
		            .andExpect(status().isOk())
		            .andReturn();
		    String orderJsonResponse = orderResult.getResponse().getContentAsString();
		    ObjectMapper objectMapper = new ObjectMapper();
		    Order order = objectMapper.readValue(orderJsonResponse, Order.class);

		    System.out.println("rerieved Order:" + order.getId());

		Order updatedOrder = updateOrderStatus(createdOrder.getId(), "COOKING");
	    assertEquals("COOKING", updatedOrder.getStatus());
	    System.out.println("Status do pedido atualizado para: " + updatedOrder.getStatus());
	    
		//updateOrderStatus(createdOrder.getId(), "READY_FOR_DELIVERY");
		//updateOrderStatus(createdOrder.getId(), "OUT_FOR_DELIVERY");
		//updateOrderStatus(createdOrder.getId(), "DELIVERED");

		Payment createdPayment = CreatePayment();
		
		updateOrderWithPayment(createdOrder.getId(), createdPayment.getId());
	}

		private void cleanAllTables() {
			paymentRepository.deleteAll();
			pizzaRepository.deleteAll();
			deliveryPersonRepository.deleteAll();
			employeeRepository.deleteAll();
			clientRepository.deleteAll();
		}

		private void createAllEntitiesForOrder() {
			client = new Client(null, "Cliente Teste", "cliente@test.com", "123456789", "Rua A");
			clientRepository.save(client);

			pizza = new Pizza(null, "Pizza Teste", "Descrição da pizza", 25.0);
			pizzaRepository.save(pizza);

			deliveryPerson = new DeliveryPerson(null, "Entregador Teste", "987654321");
			deliveryPersonRepository.save(deliveryPerson);

			employee = new Employee(null, "Funcionário Teste", "Gerente", "funcionario", "senha");
			employeeRepository.save(employee);
		}

		private Order createOrderWithoutPayment() throws Exception {
			MvcResult result = mockMvc.perform(post("/api/orders").contentType("application/json").content(String.format("""
					    {
					        "client": {"id": %d},
					        "pizzas": [{"id": %d}],
					        "deliveryPerson": {"id": %d},
					        "responsibleEmployee": {"id": %d},
					        "orderDate": "2024-10-06 12:47:47",
					        "status": "RECEIVED",
					        "totalAmount": 19.99
					    }
					""", client.getId(), pizza.getId(), deliveryPerson.getId(), employee.getId())))
					.andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("RECEIVED"))
					.andExpect(jsonPath("$.totalAmount").value(19.99)).andReturn();

			// Recupera a ordem criada a partir da resposta
			String jsonResponse = result.getResponse().getContentAsString();
			ObjectMapper objectMapper = new ObjectMapper();
			Order createdOrder = objectMapper.readValue(jsonResponse, Order.class);

			return createdOrder;
		}

		private Payment CreatePayment() throws Exception {
			MvcResult result = mockMvc
					.perform(post("/api/payments").contentType("application/json").content(
							"{\"amount\":100.0,\"paymentMethod\":\"Credit Card\",\"paymentDate\":\"2024-10-06 12:00:00\"}"))
					.andExpect(status().isCreated()).andExpect(jsonPath("$.amount").value(100.0))
					.andExpect(jsonPath("$.paymentMethod").value("Credit Card"))
					.andExpect(jsonPath("$.paymentDate").value("2024-10-06 12:00:00")).andReturn();

			String jsonResponse = result.getResponse().getContentAsString();
			ObjectMapper objectMapper = new ObjectMapper();
			Payment createdPayment = objectMapper.readValue(jsonResponse, Payment.class);

			return createdPayment;
		}
		
		
		private void updateOrderWithPayment(Long orderId, Long paymentId) throws Exception {
		    MvcResult orderResult = mockMvc.perform(get("/api/orders/" + orderId))
		            .andExpect(status().isOk())
		            .andReturn();
		    String orderJsonResponse = orderResult.getResponse().getContentAsString();
		    ObjectMapper objectMapper = new ObjectMapper();
		    Order order = objectMapper.readValue(orderJsonResponse, Order.class);
		    
		    MvcResult paymentResult = mockMvc.perform(get("/api/payments/" + paymentId))
		            .andExpect(status().isOk())
		            .andReturn();
		    String paymentJsonResponse = paymentResult.getResponse().getContentAsString();
		    Payment payment = objectMapper.readValue(paymentJsonResponse, Payment.class);
		    
		    order.setPayment(payment);
		    
		    mockMvc.perform(put("/api/orders")
		            .contentType("application/json")
		            .content(objectMapper.writeValueAsString(order))) 
		            .andExpect(status().isOk());
		}
		
		
		private Order updateOrderStatus(Long orderId, String status) throws Exception {
		    MvcResult result = mockMvc.perform(get("/api/orders/" + orderId))
		            .andExpect(status().isOk())
		            .andReturn();
		    String jsonResponse = result.getResponse().getContentAsString();
		    ObjectMapper objectMapper = new ObjectMapper();
		    Order orderToUpdate = objectMapper.readValue(jsonResponse, Order.class);

		    orderToUpdate.setStatus(status);
		    
		    String uri = switch (status) {
	        case "RECEIVED" -> "/api/orders/" + orderId + "/received";
	        case "COOKING" -> "/api/orders/" + orderId + "/cooking";
	        case "READY_FOR_DELIVERY" -> "/api/orders/" + orderId + "/ready";
	        case "OUT_FOR_DELIVERY" -> "/api/orders/" + orderId + "/out-for-delivery";
	        case "DELIVERED" -> "/api/orders/" + orderId + "/delivered";
	        default -> throw new IllegalArgumentException("Status inválido: " + status);
		    };

		    mockMvc.perform(put(uri)
		            .contentType("application/json")
		            .content(objectMapper.writeValueAsString(orderToUpdate))) 
		            .andExpect(status().isOk());

		    result = mockMvc.perform(get("/api/orders/" + orderId))
		            .andExpect(status().isOk())
		            .andReturn();

		    jsonResponse = result.getResponse().getContentAsString();
		    Order updatedOrder = objectMapper.readValue(jsonResponse, Order.class);

		   return updatedOrder;
		}



}
