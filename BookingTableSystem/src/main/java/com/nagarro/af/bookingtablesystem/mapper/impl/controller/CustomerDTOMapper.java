package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.mapper.DTOResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTOMapper extends DTOResponseMapper<CustomerDTO, CustomerResponse> {

    public CustomerDTOMapper() {
    }

    @Override
    public CustomerResponse mapDTOToResponse(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, CustomerResponse.class);
    }
}
