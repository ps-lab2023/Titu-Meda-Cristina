package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.User;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    private static final UUID CUSTOMER_UUID_TEST = UUID.fromString(TestDataBuilder.CUSTOMER_ID);

    private static final String ENCRYPTED_PASSWORD = "$2a$10$YzGNtx2VeQUW.ml7JEGWP.u6s5kXhCgxTB4t/Jykc8U.6LRkn7ORS";

    private static final Customer CUSTOMER_TEST = TestDataBuilder.buildCustomer();

    private static final CustomerDTO CUSTOMER_DTO_TEST = TestDataBuilder.buildCustomerDTO();

    private static final Comparator<User> USER_COMPARATOR = TestComparators.USER_COMPARATOR;

    private static final Comparator<UserDTO> USER_DTO_COMPARATOR = TestComparators.USER_DTO_COMPARATOR;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ListMapper listMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Test
    public void testSave_success() {
        CUSTOMER_TEST.setId(CUSTOMER_UUID_TEST);
        CUSTOMER_DTO_TEST.setId(CUSTOMER_UUID_TEST);

        Customer customerEncryptedPassword = getCustomerEncryptedPassword();

        CustomerDTO customerDTOEncryptedPassword = getCustomerDTOEncryptedPassword();

        when(modelMapper.map(CUSTOMER_DTO_TEST, Customer.class)).thenReturn(CUSTOMER_TEST);
        when(passwordEncoder.encode(CUSTOMER_TEST.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(customerRepository.save(customerArgumentCaptor.capture())).thenReturn(customerEncryptedPassword);
        when(modelMapper.map(customerEncryptedPassword, CustomerDTO.class)).thenReturn(customerDTOEncryptedPassword);

        CustomerDTO returnedCustomerDTO = customerService.save(CUSTOMER_DTO_TEST);

        assertNotNull(returnedCustomerDTO);
        assertThat(USER_COMPARATOR.compare(customerEncryptedPassword, customerArgumentCaptor.getValue())).isZero();
        assertThat(USER_DTO_COMPARATOR.compare(returnedCustomerDTO, customerDTOEncryptedPassword)).isZero();
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
        CUSTOMER_TEST.setId(CUSTOMER_UUID_TEST);
        CUSTOMER_DTO_TEST.setId(CUSTOMER_UUID_TEST);

        when(customerRepository.findById(CUSTOMER_UUID_TEST)).thenReturn(Optional.of(CUSTOMER_TEST));
        when(modelMapper.map(CUSTOMER_TEST, CustomerDTO.class)).thenReturn(CUSTOMER_DTO_TEST);

        CustomerDTO returnedCustomerDTO = customerService.findById(CUSTOMER_UUID_TEST);

        assertNotNull(returnedCustomerDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedCustomerDTO, CUSTOMER_DTO_TEST)).isZero();
    }

    @Test
    public void testFindById_notFound() {
        when(customerRepository.findById(CUSTOMER_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.findById(CUSTOMER_UUID_TEST));
    }

    @Test
    public void testFindByEmail_success() {
        CUSTOMER_TEST.setId(CUSTOMER_UUID_TEST);
        CUSTOMER_DTO_TEST.setId(CUSTOMER_UUID_TEST);

        when(customerRepository.findByEmail(CUSTOMER_TEST.getEmail())).thenReturn(Optional.of(CUSTOMER_TEST));
        when(modelMapper.map(CUSTOMER_TEST, CustomerDTO.class)).thenReturn(CUSTOMER_DTO_TEST);

        CustomerDTO returnedCustomerDTO = customerService.findByEmail(CUSTOMER_TEST.getEmail());

        assertNotNull(returnedCustomerDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedCustomerDTO, CUSTOMER_DTO_TEST)).isZero();
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

    @NotNull
    private Customer getCustomerEncryptedPassword() {
        Customer customerEncryptedPassword = TestDataBuilder.buildCustomer();
        customerEncryptedPassword.setId(CUSTOMER_UUID_TEST);
        customerEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);
        return customerEncryptedPassword;
    }

    @NotNull
    private CustomerDTO getCustomerDTOEncryptedPassword() {
        CustomerDTO customerDTOEncryptedPassword = TestDataBuilder.buildCustomerDTO();
        customerDTOEncryptedPassword.setId(CUSTOMER_UUID_TEST);
        customerDTOEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);
        return customerDTOEncryptedPassword;
    }
}
