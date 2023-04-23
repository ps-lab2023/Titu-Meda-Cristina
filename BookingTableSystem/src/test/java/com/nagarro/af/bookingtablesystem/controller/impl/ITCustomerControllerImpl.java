package com.nagarro.af.bookingtablesystem.controller.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.handler.ApiException;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.CustomerDTOMapper;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerControllerImpl.class)
public class ITCustomerControllerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private RestaurantManagerService restaurantManagerService;

    @MockBean
    private CustomerDTOMapper customerDTOMapper;

    @Test
    public void testSave_whenValidInput_thenReturnStatus201() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        CustomerResponse customerResponse = TestDataBuilder.buildCustomerResponse();

        // for the @UniqueEmail validation annotation
        when(restaurantManagerService.findByEmail(customerDTO.getEmail())).thenReturn(TestDataBuilder.buildRestaurantManagerDTO());
        when(adminService.findByEmail(customerDTO.getEmail())).thenReturn(TestDataBuilder.buildAdminDTO());
        when(customerService.findByEmail(customerDTO.getEmail())).thenThrow(NotFoundException.class);

        when(customerService.save(customerDTO)).thenReturn(customerDTO);
        when(customerDTOMapper.mapDTOToResponse(customerDTO)).thenReturn(customerResponse);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(customerResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(customerResponse.getFullName()))
                .andExpect(jsonPath("email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(customerResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(customerResponse.getCountry()))
                .andExpect(jsonPath("city").value(customerResponse.getCity()));
    }

    @Test
    public void testSave_whenInvalidInput_thenReturnStatus400() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setUsername("");

        // for the @UniqueEmail validation annotation
        when(restaurantManagerService.findByEmail(customerDTO.getEmail())).thenReturn(TestDataBuilder.buildRestaurantManagerDTO());
        when(adminService.findByEmail(customerDTO.getEmail())).thenReturn(TestDataBuilder.buildAdminDTO());
        when(customerService.findByEmail(customerDTO.getEmail())).thenThrow(NotFoundException.class);

        when(customerService.save(customerDTO)).thenReturn(customerDTO);

        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.BAD_REQUEST, "Username is mandatory!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindById_whenValidInput_thenReturnStatus200() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));
        CustomerResponse customerResponse = TestDataBuilder.buildCustomerResponse();

        when(customerService.findById(customerDTO.getId())).thenReturn(customerDTO);
        when(customerDTOMapper.mapDTOToResponse(customerDTO)).thenReturn(customerResponse);

        mockMvc.perform(get("/customers/" + customerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(customerResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(customerResponse.getFullName()))
                .andExpect(jsonPath("email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(customerResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(customerResponse.getCountry()))
                .andExpect(jsonPath("city").value(customerResponse.getCity()));
    }

    @Test
    public void testFindById_whenInvalidId_thenReturn404() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));

        when(customerService.findById(customerDTO.getId())).thenThrow(new NotFoundException("Customer with id " + customerDTO.getId()
                + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/customers/" + customerDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Customer with id " + customerDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindAll_returnStatus200() throws Exception {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTOList.add(customerDTO);

        List<CustomerResponse> customerResponseList = new ArrayList<>();
        CustomerResponse CustomerResponse = TestDataBuilder.buildCustomerResponse();
        customerResponseList.add(CustomerResponse);

        when(customerService.findAll()).thenReturn(customerDTOList);
        when(customerDTOMapper.mapDTOListToResponseList(customerDTOList)).thenReturn(customerResponseList);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(CustomerResponse.getUsername()))
                .andExpect(jsonPath("$[0].fullName").value(CustomerResponse.getFullName()))
                .andExpect(jsonPath("$[0].email").value(CustomerResponse.getEmail()))
                .andExpect(jsonPath("$[0].phoneNo").value(CustomerResponse.getPhoneNo()))
                .andExpect(jsonPath("$[0].country").value(CustomerResponse.getCountry()))
                .andExpect(jsonPath("$[0].city").value(CustomerResponse.getCity()));
    }

    @Test
    public void testDelete_whenValidId_thenReturnStatus200() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));

        mockMvc.perform(delete("/customers/" + customerDTO.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete_whenInvalidId_thenReturnStatus404() throws Exception {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));

        when(customerService.findById(customerDTO.getId())).thenThrow(new NotFoundException("Customer with id " + customerDTO.getId() + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/customers/" + customerDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Customer with id " + customerDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}
