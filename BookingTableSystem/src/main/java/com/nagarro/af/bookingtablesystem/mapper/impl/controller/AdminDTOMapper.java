package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.AdminResponse;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.mapper.DTOResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class AdminDTOMapper extends DTOResponseMapper<AdminDTO, AdminResponse> {

    public AdminDTOMapper() {
    }

    @Override
    public AdminResponse mapDTOToResponse(AdminDTO adminDTO) {
        return modelMapper.map(adminDTO, AdminResponse.class);
    }
}
