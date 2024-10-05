package com.pizzaonline.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.repository.ClientRepository;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<List<Client>> listarClientes() {
        List<Client> clientes = clientRepository.findAll();
        return ResponseEntity.ok(clientes);  
    }

    @PostMapping
    public ResponseEntity<Client> criarCliente(@RequestBody Client client) {
        Client novoCliente = clientRepository.save(client);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)  
            .body(novoCliente);          
    }

}
