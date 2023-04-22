package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantManagerMapper;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantManagerServiceImpl implements RestaurantManagerService {

    private final RestaurantManagerRepository managerRepository;

    private final RestaurantManagerMapper managerMapper;

    public RestaurantManagerServiceImpl(RestaurantManagerRepository managerRepository, RestaurantManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public RestaurantManagerDTO save(RestaurantManagerDTO restaurantManagerDTO) {
        RestaurantManager restaurantManager = managerMapper.mapDTOtoEntity(restaurantManagerDTO);
        return managerMapper.mapEntityToDTO(managerRepository.save(restaurantManager));
    }

    @Override
    public RestaurantManagerDTO findById(UUID id) {
        return managerRepository.findById(id)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with id "
                        + id + " could not be found!"));
    }

    @Override
    public RestaurantManagerDTO findByEmail(String email) {
        return managerRepository.findByEmail(email)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with email "
                        + email + " could not be found!"));
    }

    @Override
    public List<RestaurantManagerDTO> findAll() {
        List<RestaurantManager> managers = managerRepository.findAll();
        return managerMapper.mapEntityListToDTOList(managers);
    }

    @Override
    public void delete(UUID id) {
        managerRepository.deleteById(id);
    }

    private RestaurantManagerDTO mapToRestaurantManagerDTO(RestaurantManager restaurantManager) {
        return managerMapper.mapEntityToDTO(restaurantManager);
    }
}
