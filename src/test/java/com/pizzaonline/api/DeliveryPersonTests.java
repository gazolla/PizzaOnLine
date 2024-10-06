package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzaonline.api.model.DeliveryPerson;
import com.pizzaonline.api.model.Order;
import com.pizzaonline.api.model.Payment;
import com.pizzaonline.api.repository.DeliveryPersonRepository;

@SpringBootTest
class DeliveryPersonTests {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Test
    public void testPaymentSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeliveryPerson deliveryPerson = new DeliveryPerson(null, "John Doe", "123456789");
        String json = objectMapper.writeValueAsString(deliveryPerson);
        System.out.println(json);
        assertNotNull(json);
    }
    
    
    @Test
    void shouldCreateAndFindDeliveryPerson() {
        // Criação de um novo DeliveryPerson
        DeliveryPerson newDeliveryPerson = new DeliveryPerson(null, "John Doe", "123456789");
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(newDeliveryPerson);

        // Verificações após salvar
        assertNotNull(savedDeliveryPerson);
        assertNotNull(savedDeliveryPerson.getId());
        assertEquals("John Doe", savedDeliveryPerson.getName());
        assertEquals("123456789", savedDeliveryPerson.getPhone());

        // Busca do DeliveryPerson pelo ID
        DeliveryPerson foundDeliveryPerson = deliveryPersonRepository.findById(savedDeliveryPerson.getId()).orElse(null);
        assertNotNull(foundDeliveryPerson);
        assertEquals(savedDeliveryPerson.getId(), foundDeliveryPerson.getId());
    }
}
