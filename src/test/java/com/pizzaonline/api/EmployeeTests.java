package com.pizzaonline.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pizzaonline.api.model.Employee;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeTests {

	@Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateEmployee() {
        Employee newEmployee = new Employee(null, "Alice Johnson", "Manager", "alice.j", "password123");
        ResponseEntity<Employee> employeeResponse = restTemplate.postForEntity("/api/employees", newEmployee, Employee.class);
        
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        Employee registeredEmployee = employeeResponse.getBody();
        assertNotNull(registeredEmployee.getId());
    }

    @Test
    void shouldListEmployees() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/employees", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
