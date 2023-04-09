package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, ListMapper listMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        return modelMapper.map(customerRepository.save(customer), CustomerDTO.class);
    }

    @Override
    public CustomerDTO findById(UUID id) {
        return customerRepository.findById(id)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("CustomerServiceImpl: Customer with id " + id +
                        " could not be found!"));
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("CustomerServiceImpl: Customer with email " + email +
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
