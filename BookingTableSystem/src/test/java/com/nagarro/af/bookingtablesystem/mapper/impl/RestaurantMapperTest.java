package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperTest;
import com.nagarro.af.bookingtablesystem.model.Menu;
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

public class RestaurantMapperTest extends MapperTest {

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;


    @Test
    public void whenGivenRestaurantWithMenu_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());
        restaurant.setMenu(menu);

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());
        restaurantDTO.setMenuDTO(menuDTO);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);
        MenuDTO mappedMenu = mappedRestaurant.getMenuDTO();

        assertNotNull(mappedMenu);
        assertThat(menuDTOComparator.compare(menuDTO, mappedMenu)).isZero();
        assertArrayEquals(menuDTO.getContent(), mappedMenu.getContent());

        assertNotNull(mappedRestaurant);
        assertNull(mappedRestaurant.getRestaurantManagerDTO());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));
        restaurantManager.addRestaurant(restaurant);

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));
        restaurantManagerDTO.addRestaurantId(restaurant.getId());

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setRestaurantManagerDTO(restaurantManagerDTO);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);
        RestaurantManagerDTO mappedManager = mappedRestaurant.getRestaurantManagerDTO();

        assertNotNull(mappedManager);
        assertThat(userDtoComparator.compare(restaurantManagerDTO, mappedManager)).isZero();
        assertIterableEquals(restaurantManagerDTO.getRestaurantIds(), mappedManager.getRestaurantIds());

        assertNotNull(mappedRestaurant);
        assertNull(mappedRestaurant.getMenuDTO());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithMenuAndManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());
        restaurant.setMenu(menu);

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID));
        restaurantManager.addRestaurant(restaurant);

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurantManager.getId());
        restaurantManagerDTO.addRestaurantId(restaurant.getId());

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setRestaurantManagerDTO(restaurantManagerDTO);

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());
        restaurantDTO.setMenuDTO(menuDTO);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);
        MenuDTO mappedMenu = mappedRestaurant.getMenuDTO();
        RestaurantManagerDTO mappedManager = mappedRestaurant.getRestaurantManagerDTO();

        assertNotNull(mappedMenu);
        assertThat(menuDTOComparator.compare(menuDTO, mappedMenu)).isZero();
        assertArrayEquals(menuDTO.getContent(), mappedMenu.getContent());

        assertNotNull(mappedManager);
        assertThat(userDtoComparator.compare(restaurantManagerDTO, mappedManager)).isZero();
        assertIterableEquals(restaurantManagerDTO.getRestaurantIds(), mappedManager.getRestaurantIds());

        assertNotNull(mappedRestaurant);
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithNullMenuAndManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);

        assertNotNull(mappedRestaurant);
        assertNull(mappedRestaurant.getMenuDTO());
        assertNull(mappedRestaurant.getRestaurantManagerDTO());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenNullRestaurant_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantMapper.mapEntityToDTO(null));
    }

    @Test
    public void whenGivenRestaurantDTOWithMenu_thenReturnEntityWithSameFieldValues() {
        Restaurant restaurant = restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());
        restaurant.setMenu(menu);

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());
        restaurantDTO.setMenuDTO(menuDTO);

        Restaurant mappedRestaurantDTO = restaurantMapper.mapDTOtoEntity(restaurantDTO);

        Menu mappedMenuDTO = mappedRestaurantDTO.getMenu();

        assertNotNull(mappedMenuDTO);
        assertThat(menuComparator.compare(menu, mappedMenuDTO)).isZero();
        assertArrayEquals(menu.getContent(), mappedMenuDTO.getContent());
        assertEquals(menu.getRestaurant(), mappedMenuDTO.getRestaurant());

        assertNotNull(mappedRestaurantDTO);
        assertNull(mappedRestaurantDTO.getRestaurantManager());
        assertThat(restaurantComparator.compare(restaurant, mappedRestaurantDTO)).isZero();
        assertTrue(mappedRestaurantDTO.getDateCapacityAvailability().isEmpty());
    }

    @Test
    public void whenGivenRestaurantDTOWithManager_thenReturnEntityWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);
        restaurant.setRestaurantManager(restaurantManager);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);

        RestaurantManagerDTO restaurantManagerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        restaurantManagerDTO.setId(restaurantManager.getId());
        restaurantManagerDTO.addRestaurantId(restaurant.getId());

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setRestaurantManagerDTO(restaurantManagerDTO);

        Restaurant mappedRestaurantDTO = restaurantMapper.mapDTOtoEntity(restaurantDTO);

        RestaurantManager mappedManagerDTO = mappedRestaurantDTO.getRestaurantManager();

        assertNotNull(mappedManagerDTO);
        assertThat(userComparator.compare(restaurantManager, mappedManagerDTO)).isZero();
        System.out.println(mappedManagerDTO.getRestaurants());
        assertIterableEquals(restaurantManager.getRestaurants(), mappedManagerDTO.getRestaurants());

        assertNotNull(mappedRestaurantDTO);
        assertThat(restaurantComparator.compare(restaurant, mappedRestaurantDTO)).isZero();
        assertTrue(mappedRestaurantDTO.getDateCapacityAvailability().isEmpty());
    }

    @Test
    public void whenGivenRestaurantDTOWithNullMenuAndManager_thenReturnEntityWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());

        Restaurant mappedRestaurantDTO = restaurantMapper.mapDTOtoEntity(restaurantDTO);

        assertNotNull(mappedRestaurantDTO);
        assertNull(mappedRestaurantDTO.getMenu());
        assertNull(mappedRestaurantDTO.getRestaurantManager());
        assertThat(restaurantComparator.compare(restaurant, mappedRestaurantDTO)).isZero();
        assertTrue(mappedRestaurantDTO.getDateCapacityAvailability().isEmpty());
    }

    @Test
    public void whenGivenNullRestaurantDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantMapper.mapDTOtoEntity(null));
    }
}
