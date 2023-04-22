package com.nagarro.af.bookingtablesystem.mapper.impl.service;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMapper implements EntityDTOMapper<Restaurant, RestaurantDTO> {

    private final ModelMapper modelMapper;

    private final MenuMapper menuMapper;

    private final RestaurantManagerRepository restaurantManagerRepository;

    public RestaurantMapper(MenuMapper menuMapper, RestaurantManagerRepository restaurantManagerRepository) {
        this.menuMapper = menuMapper;
        this.restaurantManagerRepository = restaurantManagerRepository;
        this.modelMapper = new ModelMapper();
        configureMapper();
    }

    private void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Menu, MenuDTO> menuDTOConverter =
                ctx -> ctx.getSource() == null ? null : menuMapper.mapEntityToDTO(ctx.getSource());

        Converter<MenuDTO, Menu> dtoMenuConverter =
                ctx -> ctx.getSource() == null ? null : menuMapper.mapDTOtoEntity(ctx.getSource());

        Converter<RestaurantManager, UUID> managerDTOConverter =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

        Converter<UUID, RestaurantManager> dtoRestaurantManagerConverter =
                ctx -> ctx.getSource() == null ? null : restaurantManagerRepository.findById(ctx.getSource()).orElse(null);


        TypeMap<Restaurant, RestaurantDTO> restaurantDTOTypeMap = modelMapper.createTypeMap(Restaurant.class, RestaurantDTO.class);
        restaurantDTOTypeMap.addMappings(mapper -> {
            mapper.using(managerDTOConverter)
                    .map(
                            Restaurant::getRestaurantManager,
                            RestaurantDTO::setRestaurantManagerId
                    );
            mapper.using(menuDTOConverter)
                    .map(
                            Restaurant::getMenu,
                            RestaurantDTO::setMenuDTO
                    );
        });

        TypeMap<RestaurantDTO, Restaurant> dtoRestaurantTypeMap = modelMapper.createTypeMap(RestaurantDTO.class, Restaurant.class);
        dtoRestaurantTypeMap.addMappings(mapper -> {
            mapper.using(dtoRestaurantManagerConverter)
                    .map(
                            RestaurantDTO::getRestaurantManagerId,
                            Restaurant::setRestaurantManager

                    );
            mapper.using(dtoMenuConverter)
                    .map(
                            RestaurantDTO::getMenuDTO,
                            Restaurant::setMenu
                    );
        });
    }

    @Override
    public RestaurantDTO mapEntityToDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }

    @Override
    public Restaurant mapDTOtoEntity(RestaurantDTO restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }
}
