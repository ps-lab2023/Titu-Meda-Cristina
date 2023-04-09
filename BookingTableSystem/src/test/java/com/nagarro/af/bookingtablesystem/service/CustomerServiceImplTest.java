package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.service.impl.CustomerServiceImpl;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    private static final UUID CUSTOMER_UUID_TEST = UUID.fromString(TestDataBuilder.CUSTOMER_ID);
    private static final Customer CUSTOMER_TEST = TestDataBuilder.buildCustomer();
    private static final CustomerDTO CUSTOMER_DTO_TEST = TestDataBuilder.buildCustomerDTO();

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ListMapper listMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Test
    public void testSave_success() {
        when(modelMapper.map(CUSTOMER_DTO_TEST, Customer.class)).thenReturn(CUSTOMER_TEST);
        when(customerRepository.save(CUSTOMER_TEST)).thenReturn(CUSTOMER_TEST);
        when(modelMapper.map(CUSTOMER_TEST, CustomerDTO.class)).thenReturn(CUSTOMER_DTO_TEST);

        CustomerDTO returnedCustomerDTO = customerService.save(CUSTOMER_DTO_TEST);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        assertNotNull(returnedCustomerDTO);
        assertThat(customerArgumentCaptor.getValue().getId()).isNotNull();
        assertThat(customerArgumentCaptor.getValue().getUsername()).isEqualTo(CUSTOMER_TEST.getUsername());
        assertThat(customerArgumentCaptor.getValue().getPassword()).isEqualTo(CUSTOMER_TEST.getPassword());
        assertThat(customerArgumentCaptor.getValue().getFullName()).isEqualTo(CUSTOMER_TEST.getFullName());
        assertThat(customerArgumentCaptor.getValue().getEmail()).isEqualTo(CUSTOMER_TEST.getEmail());
        assertThat(customerArgumentCaptor.getValue().getPhoneNo()).isEqualTo(CUSTOMER_TEST.getPhoneNo());
        assertThat(customerArgumentCaptor.getValue().getCountry()).isEqualTo(CUSTOMER_TEST.getCountry());
        assertThat(customerArgumentCaptor.getValue().getCity()).isEqualTo(CUSTOMER_TEST.getCity());
    }

    @Test
    public void testSave_returnNull() {
        when(modelMapper.map(CUSTOMER_DTO_TEST, Customer.class)).thenReturn(CUSTOMER_TEST);
        when(customerRepository.save(CUSTOMER_TEST)).thenReturn(null);
        when(modelMapper.map(null, CustomerDTO.class)).thenReturn(null);

        CustomerDTO returnedCustomerDTO = customerService.save(CUSTOMER_DTO_TEST);

        assertNull(returnedCustomerDTO);
    }

    @Test
    public void testFindById_success() {
        when(customerRepository.findById(CUSTOMER_UUID_TEST)).thenReturn(Optional.of(CUSTOMER_TEST));
        when(modelMapper.map(CUSTOMER_TEST, CustomerDTO.class)).thenReturn(CUSTOMER_DTO_TEST);

        CustomerDTO returnedCustomerDTO = customerService.findById(CUSTOMER_UUID_TEST);

        assertCustomerDTOisValid(returnedCustomerDTO);
    }

    @Test
    public void testFindById_notFound() {
        when(customerRepository.findById(CUSTOMER_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.findById(CUSTOMER_UUID_TEST));
    }

    @Test
    public void testFindByEmail_success() {
        when(customerRepository.findByEmail(CUSTOMER_TEST.getEmail())).thenReturn(Optional.of(CUSTOMER_TEST));
        when(modelMapper.map(CUSTOMER_TEST, CustomerDTO.class)).thenReturn(CUSTOMER_DTO_TEST);

        CustomerDTO returnedCustomerDTO = customerService.findByEmail(CUSTOMER_TEST.getEmail());

        assertCustomerDTOisValid(returnedCustomerDTO);
    }

    @Test
    public void testFindByEmail_notFound() {
        when(customerRepository.findByEmail(CUSTOMER_TEST.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.findByEmail(CUSTOMER_TEST.getEmail()));
    }

    @Test
    public void testFindAll_success() {
        List<Customer> customers = new ArrayList<>();
        customers.add(CUSTOMER_TEST);
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        customerDTOS.add(CUSTOMER_DTO_TEST);

        when(customerRepository.findAll()).thenReturn(customers);
        when(listMapper.mapList(customers, CustomerDTO.class)).thenReturn(customerDTOS);

        List<CustomerDTO> returnedCustomerDTOList = customerService.findAll();

        assertNotNull(returnedCustomerDTOList);
        assertTrue(returnedCustomerDTOList.containsAll(customerDTOS));
    }

    @Test
    public void testFindAll_returnEmptyList() {
        List<Customer> customers = new ArrayList<>();

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> returnedCustomerDTOList = customerService.findAll();

        assertTrue(returnedCustomerDTOList.isEmpty());
    }

    private void assertCustomerDTOisValid(CustomerDTO customerDTO) {
        assertNotNull(customerDTO.getId());
        assertEquals(customerDTO.getUsername(), CUSTOMER_DTO_TEST.getUsername());
        assertEquals(customerDTO.getPassword(), CUSTOMER_DTO_TEST.getPassword());
        assertEquals(customerDTO.getFullName(), CUSTOMER_DTO_TEST.getFullName());
        assertEquals(customerDTO.getEmail(), CUSTOMER_DTO_TEST.getEmail());
        assertEquals(customerDTO.getPhoneNo(), CUSTOMER_DTO_TEST.getPhoneNo());
        assertEquals(customerDTO.getCountry(), CUSTOMER_DTO_TEST.getCountry());
        assertEquals(customerDTO.getCity(), CUSTOMER_DTO_TEST.getCity());
    }
}
