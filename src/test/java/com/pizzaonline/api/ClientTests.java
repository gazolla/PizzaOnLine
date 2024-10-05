package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientTests {

	@Autowired
    private TestRestTemplate restTemplate;

	
	@Test
	void shouldCreateClient() {
		Client novoCliente = new Client(null, "João Silva", "joao@email.com", "123456789", "Rua A, 123", new HashSet<Order>());
        ResponseEntity<Client> clienteResponse = restTemplate.postForEntity("/api/clients", novoCliente, Client.class);
        assertEquals(HttpStatus.CREATED, clienteResponse.getStatusCode());
        Client clienteCadastrado = clienteResponse.getBody();
        assertNotNull(clienteCadastrado.getId());
	}
	
	@Test
    void shouldListClients() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/clients", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
