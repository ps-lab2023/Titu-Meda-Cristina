package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ITCustomerRepository extends ITBaseRepository {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByEmail_success() {
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getEmail());
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail("nothing");
        assertTrue(optionalCustomer.isEmpty());
    }
}
