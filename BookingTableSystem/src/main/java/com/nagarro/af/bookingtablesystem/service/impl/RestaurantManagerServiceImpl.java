package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantManagerMapper;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantManagerServiceImpl implements RestaurantManagerService {

    private final RestaurantManagerRepository managerRepository;

    private final RestaurantManagerMapper managerMapper;

    private final PasswordEncoder passwordEncoder;

    public RestaurantManagerServiceImpl(RestaurantManagerRepository managerRepository, RestaurantManagerMapper managerMapper, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RestaurantManagerDTO save(RestaurantManagerDTO restaurantManagerDTO) {
        RestaurantManager restaurantManager = managerMapper.mapDTOtoEntity(restaurantManagerDTO);
        restaurantManager.setPassword(passwordEncoder.encode(restaurantManager.getPassword()));
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
    public RestaurantManagerDTO findByUsername(String username) {
        return managerRepository.findByUsername(username)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with username "
                        + username + " could not be found!"));
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
