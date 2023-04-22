package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantDTO save(RestaurantDTO restaurantDTO, MultipartFile menu);

    RestaurantDTO findById(UUID id);

    RestaurantDTO findByName(String name);

    List<RestaurantDTO> findAll();

    List<RestaurantDTO> findAllByCountryAndCity(String country, String city);

    void delete(UUID id);

    void assignRestaurantManager(UUID restaurantId, UUID managerId);
}
