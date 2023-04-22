package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.RestaurantController;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantControllerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public ResponseEntity<RestaurantDTO> save(RestaurantDTO restaurantDTO, MultipartFile menu) {
        return new ResponseEntity<>(restaurantService.save(restaurantDTO, menu), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RestaurantDTO> findById(UUID id) {
        return new ResponseEntity<>(restaurantService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RestaurantDTO> findByName(String name) {
        return new ResponseEntity<>(restaurantService.findByName(name), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> findAll() {
        return new ResponseEntity<>(restaurantService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RestaurantDTO>> findAllByCountryAndCity(String country, String city) {
        return new ResponseEntity<>(restaurantService.findAllByCountryAndCity(country, city), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        restaurantService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> assignRestaurantManager(UUID restaurantId, UUID managerId) {
        restaurantService.assignRestaurantManager(restaurantId, managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
