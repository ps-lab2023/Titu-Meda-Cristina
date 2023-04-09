package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantDTO save(RestaurantDTO restaurantDTO);

    RestaurantDTO findById(UUID id);

    RestaurantDTO findByName(String name);

    List<RestaurantDTO> findAll();

    List<RestaurantDTO> findAllByCountryAndCity(String country, String city);

    void delete(UUID id);
}
