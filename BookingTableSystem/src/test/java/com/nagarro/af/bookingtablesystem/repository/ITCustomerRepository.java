package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("classpath:scripts/insert_customers.sql")
public class ITCustomerRepository extends ITRepository {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByEmail_success() {
        Customer customer = TestDataBuilder.buildCustomer();
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getEmail());
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail("nothing");
        assertTrue(optionalCustomer.isEmpty());
    }
}
