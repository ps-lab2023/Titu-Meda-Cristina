package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomerDTOMapper.class)
public class CustomerDTOMapperTest {

    @Autowired
    private CustomerDTOMapper customerDTOMapper;

    private final Comparator<UserResponse> customerResponseComparator = TestComparators.USER_RESPONSE_COMPARATOR;

    @Test
    public void whenGivenCustomerDTO_thenReturnResponseWithSameFieldValues() {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        CustomerResponse customerResponse = TestDataBuilder.buildCustomerResponse();

        CustomerResponse mappedCustomerDTO = customerDTOMapper.mapDTOToResponse(customerDTO);

        assertNotNull(mappedCustomerDTO);
        assertThat(customerResponseComparator.compare(customerResponse, mappedCustomerDTO)).isZero();
    }

    @Test
    public void whenGivenCustomerDTOWithNullValues_thenReturnResponseWithSameFieldValues() {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setCountry(null);
        customerDTO.setCity(null);
        CustomerResponse customerResponse = TestDataBuilder.buildCustomerResponse();
        customerResponse.setCountry(null);
        customerResponse.setCity(null);

        CustomerResponse mappedCustomerDTO = customerDTOMapper.mapDTOToResponse(customerDTO);

        assertNotNull(mappedCustomerDTO);
        assertNull(mappedCustomerDTO.getCountry());
        assertNull(mappedCustomerDTO.getCity());
        assertEquals(customerResponse.getUsername(), mappedCustomerDTO.getUsername());
        assertEquals(customerResponse.getEmail(), mappedCustomerDTO.getEmail());
        assertEquals(customerResponse.getFullName(), mappedCustomerDTO.getFullName());
        assertEquals(customerResponse.getPhoneNo(), mappedCustomerDTO.getPhoneNo());
    }

    @Test
    public void whenGivenNullCustomerDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerDTOMapper.mapDTOToResponse(null));
    }

    @Test
    public void whenGivenCustomerDTOList_thenReturnResponseListWithSameFieldValues() {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerDTOList.add(customerDTO);

        CustomerResponse customerResponse = TestDataBuilder.buildCustomerResponse();
        List<CustomerResponse> customerResponseList = new ArrayList<>();
        customerResponseList.add(customerResponse);

        List<CustomerResponse> mappedCustomerDTOList = customerDTOMapper.mapDTOListToResponseList(customerDTOList);

        assertNotNull(mappedCustomerDTOList);
        assertTrue(customerResponseList.containsAll(mappedCustomerDTOList));
    }

    @Test
    public void whenGivenCustomerDTOEmptyList_thenReturnResponseWithSameFieldValues() {
        List<CustomerResponse> mappedCustomerDTOList = customerDTOMapper.mapDTOListToResponseList(new ArrayList<>());

        assertNotNull(mappedCustomerDTOList);
        assertTrue(mappedCustomerDTOList.isEmpty());
    }
}
