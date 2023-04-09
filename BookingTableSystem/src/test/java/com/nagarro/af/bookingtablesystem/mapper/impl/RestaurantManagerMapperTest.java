package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperTest;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantManagerMapperTest extends MapperTest {

    @Autowired
    private RestaurantManagerMapper restaurantManagerMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;

    @Test
    public void whenGivenManagerWithRestaurants_thenReturnDTOWithSameFields() {
        Restaurant restaurant1 = TestDataBuilder.buildRestaurant();
        restaurant1.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        Restaurant restaurant2 = new Restaurant("Booha",
                "booha@yahoo.com",
                "+40716579199",
                "Romania",
                "Bucuresti",
                "str. Aldea 3",
                "Hunted place!",
                null,
                30,
                10
        );
        restaurant2.setId(UUID.randomUUID());

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));
        restaurantManager.addRestaurant(restaurant1);
        restaurantManager.addRestaurant(restaurant2);

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurantManager.getId());
        restaurantManagerDTO.addRestaurantId(restaurant1.getId());
        restaurantManagerDTO.addRestaurantId(restaurant2.getId());

        RestaurantManagerDTO mappedManager = restaurantManagerMapper.mapEntityToDTO(restaurantManager);

        assertNotNull(mappedManager);
        assertThat(userDtoComparator.compare(restaurantManagerDTO, mappedManager)).isZero();
        assertIterableEquals(restaurantManagerDTO.getRestaurantIds(), mappedManager.getRestaurantIds());
    }

    @Test
    public void whenGivenManagerWithEmptyRestaurantsList_thenReturnDTOWithSameFields() {
        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurantManager.getId());

        RestaurantManagerDTO mappedManager = restaurantManagerMapper.mapEntityToDTO(restaurantManager);

        assertNotNull(mappedManager);
        assertThat(userDtoComparator.compare(restaurantManagerDTO, mappedManager)).isZero();
        assertTrue(mappedManager.getRestaurantIds().isEmpty());
    }

    @Test
    public void whenGivenNullManager_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantManagerMapper.mapEntityToDTO(null));
    }

    @Test
    public void whenGivenManagerDTOWithRestaurants_thenReturnEntityWithSameFields() {
        Restaurant restaurant1 = TestDataBuilder.buildRestaurant();

        Restaurant restaurant2 = new Restaurant("Booha",
                "booha@yahoo.com",
                "+40716579199",
                "Romania",
                "Bucuresti",
                "str. Aldea 3",
                "Hunted place!",
                null,
                30,
                10
        );

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant1);
        restaurantManager.addRestaurant(restaurant2);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant1 = restaurantRepository.saveAndFlush(restaurant1);
        restaurant2 = restaurantRepository.saveAndFlush(restaurant2);

        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());
        assertTrue(restaurantRepository.findById(restaurant1.getId()).isPresent());
        assertTrue(restaurantRepository.findById(restaurant2.getId()).isPresent());

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurant1.getRestaurantManager().getId());
        restaurantManagerDTO.addRestaurantId(restaurant1.getId());
        restaurantManagerDTO.addRestaurantId(restaurant2.getId());

        RestaurantManager mappedManagerDTO = restaurantManagerMapper.mapDTOtoEntity(restaurantManagerDTO);

        assertNotNull(mappedManagerDTO);
        assertThat(userComparator.compare(restaurantManager, mappedManagerDTO)).isZero();
        assertIterableEquals(restaurantManager.getRestaurants(), mappedManagerDTO.getRestaurants());
    }

    @Test
    public void whenGivenManagerDTOWithEmptyRestaurantsList_thenReturnEntityWithSameFields() {
        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurantManager.getId());

        RestaurantManager mappedManagerDTO = restaurantManagerMapper.mapDTOtoEntity(restaurantManagerDTO);

        assertNotNull(mappedManagerDTO);
        assertThat(userComparator.compare(restaurantManager, mappedManagerDTO)).isZero();
        assertTrue(mappedManagerDTO.getRestaurants().isEmpty());
    }

    @Test
    public void whenGivenNullManagerDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantManagerMapper.mapEntityToDTO(null));
    }
}
