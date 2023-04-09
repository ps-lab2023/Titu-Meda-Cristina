package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.mapDTOtoEntity(restaurantDTO);
        return restaurantMapper.mapEntityToDTO(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantDTO findById(UUID id) {
        return restaurantRepository.findById(id)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("RestaurantServiceImpl: Restaurant with id " + id +
                        "could not be found!"));
    }

    @Override
    public RestaurantDTO findByName(String name) {
        return restaurantRepository.findByName(name)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("RestaurantServiceImpl: Restaurant with name " + name +
                        "could not be found!"));
    }

    @Override
    public List<RestaurantDTO> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurantMapper.mapEntityListToDTOList(restaurants);
    }

    @Override
    public List<RestaurantDTO> findAllByCountryAndCity(String country, String city) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity(country, city);
        return restaurantMapper.mapEntityListToDTOList(restaurants);
    }

    @Override
    public void delete(UUID id) {
        restaurantRepository.deleteById(id);
    }

    private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
        return restaurantMapper.mapEntityToDTO(restaurant);
    }
}
