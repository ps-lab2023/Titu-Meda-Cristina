package com.nagarro.af.bookingtablesystem.mapper.impl.service;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MenuMapper implements EntityDTOMapper<Menu, MenuDTO> {

    private final ModelMapper modelMapper;

    private final RestaurantRepository restaurantRepository;

    public MenuMapper(RestaurantRepository restaurantRepository) {
        this.modelMapper = new ModelMapper();
        configureMapper();
        this.restaurantRepository = restaurantRepository;
    }

    private void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<UUID, Restaurant> uuidRestaurantConverter =
                ctx -> ctx.getSource() == null ? null :
                        restaurantRepository.findById(ctx.getSource()).orElse(null);

        modelMapper.createTypeMap(MenuDTO.class, Menu.class)
                .addMappings(map -> map
                        .using(uuidRestaurantConverter)
                        .map(
                                MenuDTO::getId,
                                Menu::setRestaurant
                        )
                );
    }

    @Override
    public MenuDTO mapEntityToDTO(Menu menu) {
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public Menu mapDTOtoEntity(MenuDTO menuDTO) {
        return modelMapper.map(menuDTO, Menu.class);
    }
}
