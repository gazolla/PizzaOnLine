package com.pizzaonline.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzaonline.api.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {}