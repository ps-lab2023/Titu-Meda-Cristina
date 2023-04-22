package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.RestaurantManagerResponse;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.DTOResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantManagerDTOMapper extends DTOResponseMapper<RestaurantManagerDTO, RestaurantManagerResponse> {

    public RestaurantManagerDTOMapper() {
    }

    @Override
    public RestaurantManagerResponse mapDTOToResponse(RestaurantManagerDTO managerDTO) {
        return modelMapper.map(managerDTO, RestaurantManagerResponse.class);
    }
}
