package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.RestaurantManagerController;
import com.nagarro.af.bookingtablesystem.controller.response.RestaurantManagerResponse;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.RestaurantManagerDTOMapper;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class RestaurantManagerControllerImpl implements RestaurantManagerController {

    private final RestaurantManagerService restaurantManagerService;

    private final RestaurantManagerDTOMapper restaurantManagerDTOMapper;

    public RestaurantManagerControllerImpl(RestaurantManagerService restaurantManagerService, RestaurantManagerDTOMapper restaurantManagerDTOMapper) {
        this.restaurantManagerService = restaurantManagerService;
        this.restaurantManagerDTOMapper = restaurantManagerDTOMapper;
    }

    @Override
    public ResponseEntity<RestaurantManagerResponse> save(RestaurantManagerDTO managerDTO) {
        return new ResponseEntity<>(mapToRestaurantManagerResponse(restaurantManagerService.save(managerDTO)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RestaurantManagerResponse> findById(UUID id) {
        return new ResponseEntity<>(mapToRestaurantManagerResponse(restaurantManagerService.findById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantManagerResponse>> findAll() {
        return new ResponseEntity<>(mapToRestaurantManagerResponseList(restaurantManagerService.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        restaurantManagerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private RestaurantManagerResponse mapToRestaurantManagerResponse(RestaurantManagerDTO managerDTO) {
        return restaurantManagerDTOMapper.mapDTOToResponse(managerDTO);
    }

    private List<RestaurantManagerResponse> mapToRestaurantManagerResponseList(List<RestaurantManagerDTO> managerDTOList) {
        return restaurantManagerDTOMapper.mapDTOListToResponseList(managerDTOList);
    }
}
