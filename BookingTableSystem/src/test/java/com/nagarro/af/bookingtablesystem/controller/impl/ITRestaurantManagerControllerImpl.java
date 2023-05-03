package com.nagarro.af.bookingtablesystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af.bookingtablesystem.controller.response.RestaurantManagerResponse;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.handler.ApiException;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.RestaurantManagerDTOMapper;
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

@WebMvcTest(controllers = RestaurantManagerControllerImpl.class)
public class ITRestaurantManagerControllerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantManagerService restaurantManagerService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private RestaurantManagerDTOMapper restaurantManagerDTOMapper;

    @Test
    public void testSave_whenValidInput_thenReturnStatus201() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();

        // for the @UniqueEmail validation annotation
        when(restaurantManagerService.findByEmail(managerDTO.getEmail())).thenThrow(NotFoundException.class);
        when(restaurantManagerService.save(managerDTO)).thenReturn(managerDTO);
        when(restaurantManagerDTOMapper.mapDTOToResponse(managerDTO)).thenReturn(managerResponse);

        mockMvc.perform(post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(managerResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(managerResponse.getFullName()))
                .andExpect(jsonPath("email").value(managerResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(managerResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(managerResponse.getCountry()))
                .andExpect(jsonPath("city").value(managerResponse.getCity()));
    }

    @Test
    public void testSave_whenInvalidInput_thenReturnStatus400() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setUsername("");

        // for the @UniqueEmail validation annotation
        when(adminService.findByEmail(managerDTO.getEmail())).thenReturn(TestDataBuilder.buildAdminDTO());
        when(customerService.findByEmail(managerDTO.getEmail())).thenReturn(TestDataBuilder.buildCustomerDTO());
        when(restaurantManagerService.findByEmail(managerDTO.getEmail())).thenThrow(NotFoundException.class);

        when(restaurantManagerService.save(managerDTO)).thenReturn(managerDTO);

        MvcResult mvcResult = mockMvc.perform(post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.BAD_REQUEST, "Username is mandatory!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindById_whenValidInput_thenReturnStatus200() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));
        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();

        when(restaurantManagerService.findById(managerDTO.getId())).thenReturn(managerDTO);
        when(restaurantManagerDTOMapper.mapDTOToResponse(managerDTO)).thenReturn(managerResponse);

        mockMvc.perform(get("/managers/" + managerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(managerResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(managerResponse.getFullName()))
                .andExpect(jsonPath("email").value(managerResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(managerResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(managerResponse.getCountry()))
                .andExpect(jsonPath("city").value(managerResponse.getCity()));
    }

    @Test
    public void testFindById_whenInvalidId_thenReturn404() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));

        when(restaurantManagerService.findById(managerDTO.getId())).thenThrow(new NotFoundException("Restaurant manager with id " + managerDTO.getId()
                + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/managers/" + managerDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Restaurant manager with id " + managerDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindAll_returnStatus200() throws Exception {
        List<RestaurantManagerDTO> managerDTOList = new ArrayList<>();
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTOList.add(managerDTO);

        List<RestaurantManagerResponse> managerResponseList = new ArrayList<>();
        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();
        managerResponseList.add(managerResponse);

        when(restaurantManagerService.findAll()).thenReturn(managerDTOList);
        when(restaurantManagerDTOMapper.mapDTOListToResponseList(managerDTOList)).thenReturn(managerResponseList);

        mockMvc.perform(get("/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(managerResponse.getUsername()))
                .andExpect(jsonPath("$[0].fullName").value(managerResponse.getFullName()))
                .andExpect(jsonPath("$[0].email").value(managerResponse.getEmail()))
                .andExpect(jsonPath("$[0].phoneNo").value(managerResponse.getPhoneNo()))
                .andExpect(jsonPath("$[0].country").value(managerResponse.getCountry()))
                .andExpect(jsonPath("$[0].city").value(managerResponse.getCity()));
    }

    @Test
    public void testDelete_whenValidId_thenReturnStatus200() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));

        mockMvc.perform(delete("/managers/" + managerDTO.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete_whenInvalidId_thenReturnStatus404() throws Exception {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));

        when(restaurantManagerService.findById(managerDTO.getId())).thenThrow(new NotFoundException("Restaurant manager with id "
                + managerDTO.getId() + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/managers/" + managerDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Restaurant manager with id " + managerDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}
