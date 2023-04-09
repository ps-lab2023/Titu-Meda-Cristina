package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper implements EntityDTOMapper<Restaurant, RestaurantDTO> {

    private final ModelMapper modelMapper;

    private final RestaurantManagerMapper restaurantManagerMapper;

    private final MenuMapper menuMapper;

    public RestaurantMapper(RestaurantManagerMapper restaurantManagerMapper, MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
        this.modelMapper = new ModelMapper();
        configureMapper();
        this.restaurantManagerMapper = restaurantManagerMapper;
    }

    private void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Menu, MenuDTO> menuDTOConverter =
                ctx -> ctx.getSource() == null ? null : menuMapper.mapEntityToDTO(ctx.getSource());

        Converter<MenuDTO, Menu> dtoMenuConverter =
                ctx -> ctx.getSource() == null ? null : menuMapper.mapDTOtoEntity(ctx.getSource());

        Converter<RestaurantManager, RestaurantManagerDTO> managerDTOConverter =
                ctx -> ctx.getSource() == null ? null : restaurantManagerMapper.mapEntityToDTO(ctx.getSource());

        Converter<RestaurantManagerDTO, RestaurantManager> dtoRestaurantManagerConverter =
                ctx -> ctx.getSource() == null ? null : restaurantManagerMapper.mapDTOtoEntity(ctx.getSource());


        TypeMap<Restaurant, RestaurantDTO> restaurantDTOTypeMap = modelMapper.createTypeMap(Restaurant.class, RestaurantDTO.class);
        restaurantDTOTypeMap.addMappings(mapper -> {
            mapper.using(managerDTOConverter)
                    .map(
                            Restaurant::getRestaurantManager,
                            RestaurantDTO::setRestaurantManagerDTO
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
                            RestaurantDTO::getRestaurantManagerDTO,
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
