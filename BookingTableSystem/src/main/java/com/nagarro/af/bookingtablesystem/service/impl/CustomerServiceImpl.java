package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return modelMapper.map(customerRepository.save(customer), CustomerDTO.class);
    }

    @Override
    public CustomerDTO findById(UUID id) {
        return customerRepository.findById(id)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id +
                        " could not be found!"));
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with email " + email +
                        " could not be found!"));
    }

    @Override
    public CustomerDTO findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with username " + username +
                        " could not be found!"));
    }

    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return listMapper.mapList(customers, CustomerDTO.class);
    }

    @Override
    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
