package com.nagarro.af.bookingtablesystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af.bookingtablesystem.controller.response.AdminResponse;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.handler.ApiException;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.AdminDTOMapper;
import com.nagarro.af.bookingtablesystem.service.AdminService;
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

@WebMvcTest(controllers = AdminControllerImpl.class)
public class ITAdminControllerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AdminDTOMapper adminDTOMapper;

    @Test
    public void testSave_whenValidInput_thenReturnStatus201() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();

        when(adminService.findByEmail(adminDTO.getEmail())).thenThrow(NotFoundException.class); // for the @UniqueEmail validation annotation
        when(adminService.save(adminDTO)).thenReturn(adminDTO);
        when(adminDTOMapper.mapDTOToResponse(adminDTO)).thenReturn(adminResponse);

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(adminResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(adminResponse.getFullName()))
                .andExpect(jsonPath("email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(adminResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(adminResponse.getCountry()))
                .andExpect(jsonPath("city").value(adminResponse.getCity()));
    }

    @Test
    public void testSave_whenInvalidInput_thenReturnStatus400() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setUsername("");

        when(adminService.findByEmail(adminDTO.getEmail())).thenThrow(NotFoundException.class); // for the @UniqueEmail validation annotation
        when(adminService.save(adminDTO)).thenReturn(adminDTO);

        MvcResult mvcResult = mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.BAD_REQUEST, "Username is mandatory!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindById_whenValidId_thenReturnStatus200() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setId(UUID.fromString(TestDataBuilder.ADMIN_ID));
        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();

        when(adminService.findById(adminDTO.getId())).thenReturn(adminDTO);
        when(adminDTOMapper.mapDTOToResponse(adminDTO)).thenReturn(adminResponse);

        mockMvc.perform(get("/admins/" + adminDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(adminResponse.getUsername()))
                .andExpect(jsonPath("fullName").value(adminResponse.getFullName()))
                .andExpect(jsonPath("email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("phoneNo").value(adminResponse.getPhoneNo()))
                .andExpect(jsonPath("country").value(adminResponse.getCountry()))
                .andExpect(jsonPath("city").value(adminResponse.getCity()));
    }

    @Test
    public void testFindById_whenInvalidId_thenReturn404() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setId(UUID.fromString(TestDataBuilder.ADMIN_ID));

        when(adminService.findById(adminDTO.getId())).thenThrow(new NotFoundException("Admin with id " + adminDTO.getId()
                + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/admins/" + adminDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Admin with id " + adminDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindAll_returnStatus200() throws Exception {
        List<AdminDTO> adminDTOList = new ArrayList<>();
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTOList.add(adminDTO);

        List<AdminResponse> adminResponseList = new ArrayList<>();
        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();
        adminResponseList.add(adminResponse);

        when(adminService.findAll()).thenReturn(adminDTOList);
        when(adminDTOMapper.mapDTOListToResponseList(adminDTOList)).thenReturn(adminResponseList);

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(adminResponse.getUsername()))
                .andExpect(jsonPath("$[0].fullName").value(adminResponse.getFullName()))
                .andExpect(jsonPath("$[0].email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$[0].phoneNo").value(adminResponse.getPhoneNo()))
                .andExpect(jsonPath("$[0].country").value(adminResponse.getCountry()))
                .andExpect(jsonPath("$[0].city").value(adminResponse.getCity()));
    }

    @Test
    public void testDelete_whenValidId_thenReturnStatus200() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setId(UUID.fromString(TestDataBuilder.ADMIN_ID));

        mockMvc.perform(delete("/admins/" + adminDTO.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete_whenInvalidId_thenReturnStatus404() throws Exception {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setId(UUID.fromString(TestDataBuilder.ADMIN_ID));

        when(adminService.findById(adminDTO.getId())).thenThrow(new NotFoundException("Admin with id " + adminDTO.getId() + " could not be found!"));

        MvcResult mvcResult = mockMvc.perform(get("/admins/" + adminDTO.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND,
                "Admin with id " + adminDTO.getId() + " could not be found!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}
