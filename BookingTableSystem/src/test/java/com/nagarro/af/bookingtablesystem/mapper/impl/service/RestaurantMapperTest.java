package com.nagarro.af.bookingtablesystem.mapper.impl.service;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperBaseTest;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantMapperTest extends MapperBaseTest {

    private static final UUID MANAGER_ID = UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID);

    private static final UUID RESTAURANT_ID = UUID.fromString(TestDataBuilder.RESTAURANT_ID);

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;


    @Test
    public void whenGivenRestaurantWithMenu_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildRestaurantWithMenu();

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant);

        MenuDTO menuDTO = buildMenuDTO(restaurant);
        restaurantDTO.setMenuDTO(menuDTO);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);
        MenuDTO mappedMenu = mappedRestaurant.getMenuDTO();

        assertNotNull(mappedMenu);
        assertThat(menuDTOComparator.compare(menuDTO, mappedMenu)).isZero();
        assertArrayEquals(menuDTO.getContent(), mappedMenu.getContent());

        assertNotNull(mappedRestaurant);
        assertNull(mappedRestaurant.getRestaurantManagerId());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildRestaurantWithManager();

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant, MANAGER_ID);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);

        assertNotNull(mappedRestaurant);
        assertNotNull(mappedRestaurant.getRestaurantManagerId());
        assertEquals(MANAGER_ID, mappedRestaurant.getRestaurantManagerId());
        assertNull(mappedRestaurant.getMenuDTO());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithMenuAndManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildRestaurantWithMenuAndManager();

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant, MANAGER_ID);

        MenuDTO menuDTO = buildMenuDTO(restaurant);
        restaurantDTO.setMenuDTO(menuDTO);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);
        MenuDTO mappedMenu = mappedRestaurant.getMenuDTO();

        assertNotNull(mappedMenu);
        assertThat(menuDTOComparator.compare(menuDTO, mappedMenu)).isZero();
        assertArrayEquals(menuDTO.getContent(), mappedMenu.getContent());

        assertNotNull(mappedRestaurant);
        assertNotNull(mappedRestaurant.getRestaurantManagerId());
        assertEquals(MANAGER_ID, mappedRestaurant.getRestaurantManagerId());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenRestaurantWithNullMenuAndManager_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildRestaurant();

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant);

        RestaurantDTO mappedRestaurant = restaurantMapper.mapEntityToDTO(restaurant);

        assertNotNull(mappedRestaurant);
        assertNull(mappedRestaurant.getMenuDTO());
        assertNull(mappedRestaurant.getRestaurantManagerId());
        assertThat(restaurantDTOComparator.compare(restaurantDTO, mappedRestaurant)).isZero();
    }

    @Test
    public void whenGivenNullRestaurant_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantMapper.mapEntityToDTO(null));
    }

    @Test
    public void whenGivenRestaurantDTOWithMenu_thenReturnEntityWithSameFieldValues() {
        Restaurant restaurant = saveNewRestaurant();

        Menu menu = buildMenu(restaurant);

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant);

        MenuDTO menuDTO = buildMenuDTO(restaurant);
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

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant, restaurantManager.getId());

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
        Restaurant restaurant = buildRestaurant();

        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant);

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

    @NotNull
    private Menu buildMenu(Restaurant restaurant) {
        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());
        restaurant.setMenu(menu);
        return menu;
    }

    @NotNull
    private RestaurantManager buildRestaurantManager(Restaurant restaurant) {
        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.setId(MANAGER_ID);
        restaurantManager.addRestaurant(restaurant);
        restaurant.setRestaurantManager(restaurantManager);
        return restaurantManager;
    }

    @NotNull
    private Restaurant buildRestaurant() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(RESTAURANT_ID);
        return restaurant;
    }

    @NotNull
    private Restaurant buildRestaurantWithMenu() {
        Restaurant restaurant = buildRestaurant();
        buildMenu(restaurant);
        return restaurant;
    }

    @NotNull
    private Restaurant buildRestaurantWithManager() {
        Restaurant restaurant = buildRestaurant();
        buildRestaurantManager(restaurant);
        return restaurant;
    }

    @NotNull
    private Restaurant buildRestaurantWithMenuAndManager() {
        Restaurant restaurant = buildRestaurant();
        buildMenu(restaurant);
        buildRestaurantManager(restaurant);
        return restaurant;
    }

    @NotNull
    private MenuDTO buildMenuDTO(Restaurant restaurant) {
        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());
        return menuDTO;
    }

    @NotNull
    private RestaurantDTO buildRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        return restaurantDTO;
    }

    @NotNull
    private RestaurantDTO buildRestaurantDTO(Restaurant restaurant, UUID managerId) {
        RestaurantDTO restaurantDTO = buildRestaurantDTO(restaurant);
        restaurantDTO.setRestaurantManagerId(managerId);
        return restaurantDTO;
    }

    @NotNull
    private Restaurant saveNewRestaurant() {
        return restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());
    }
}
