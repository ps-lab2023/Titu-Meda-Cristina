package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.exception.CorruptedFileException;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.MenuMapper;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.MenuRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final RestaurantManagerRepository restaurantManagerRepository;

    private final MenuRepository menuRepository;

    private final RestaurantMapper restaurantMapper;

    private final MenuMapper menuMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 RestaurantManagerRepository restaurantManagerRepository,
                                 MenuRepository menuRepository, RestaurantMapper restaurantMapper, MenuMapper menuMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
        this.menuRepository = menuRepository;
        this.restaurantMapper = restaurantMapper;
        this.menuMapper = menuMapper;
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
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id +
                        " could not be found!"));
    }

    @Override
    public RestaurantDTO findByName(String name) {
        return restaurantRepository.findByName(name)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant with name " + name +
                        " could not be found!"));
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
    @Transactional
    public void delete(UUID id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        if (optionalRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant with id " + id + " could not be found!");
        }

        Restaurant restaurant = optionalRestaurant.get();
        RestaurantManager manager = restaurant.getRestaurantManager();

        manager.removeRestaurant(restaurant);
        restaurantManagerRepository.save(manager);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public void assignRestaurantManager(UUID restaurantId, UUID managerId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Optional<RestaurantManager> optionalManager = restaurantManagerRepository.findById(managerId);

        if (optionalRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant with id " + restaurantId +
                    " could not be found!");
        }

        if (optionalManager.isEmpty()) {
            throw new NotFoundException("Restaurant manager with id " + managerId +
                    " could not be found!");
        }

        Restaurant restaurant = optionalRestaurant.get();
        RestaurantManager manager = optionalManager.get();

        manager.addRestaurant(restaurant);
        restaurantRepository.save(restaurant);
        restaurantManagerRepository.save(manager);
    }

    @Override
    public void uploadMenu(UUID restaurantId, MultipartFile menuFile) {
        Optional<Menu> optionalMenu = menuRepository.findById(restaurantId);
        optionalMenu.ifPresent(menuRepository::delete);

        RestaurantDTO restaurantDTO = findById(restaurantId);
        MenuDTO menuDTO = null;
        if (menuFile != null) {
            menuDTO = getMenuDTO(menuFile);
        }

        Restaurant restaurant = restaurantMapper.mapDTOtoEntity(restaurantDTO);
        Menu menu = menuMapper.mapDTOtoEntity(menuDTO);
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);

        restaurantRepository.save(restaurant);
    }

    private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
        return restaurantMapper.mapEntityToDTO(restaurant);
    }

    private MenuDTO getMenuDTO(@NotNull MultipartFile menu) {
        try {
            String menuFileName = StringUtils.cleanPath(menu.getOriginalFilename());
            return new MenuDTO(menuFileName, menu.getBytes(), menu.getContentType());
        } catch (IOException e) {
            throw new CorruptedFileException("Menu file is invalid!");
        }
    }
}
