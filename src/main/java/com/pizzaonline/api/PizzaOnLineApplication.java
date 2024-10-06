package com.pizzaonline.api;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pizzaonline.api.model.Client;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.repository.ClientRepository;

@SpringBootApplication
public class PizzaOnLineApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaOnLineApplication.class, args);
	}

}



@Component
class ClientDataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco de dados já tem clientes
        if (clientRepository.count() == 0) {
            // Criação de clientes de exemplo
            Client cliente1 = new Client(null, "João Silva", "joao@email.com", "123456789", "Rua A, 123");
            Client cliente2 = new Client(null, "Maria Souza", "maria@email.com", "987654321", "Rua B, 456");
            Client cliente3 = new Client(null, "Pedro Santos", "pedro@email.com", "1122334455", "Rua C, 789");

            // Salvando clientes no banco
            clientRepository.save(cliente1);
            clientRepository.save(cliente2);
            clientRepository.save(cliente3);
            
            System.out.println("Clientes de exemplo carregados no banco de dados.");
        } else {
            System.out.println("Clientes já existentes, nenhum dado carregado.");
        }
    }
}
