package com.nagarro.af.bookingtablesystem.mapper.impl.service;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperBaseTest;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MenuMapperTest extends MapperBaseTest {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void whenGivenMenu_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildNewRestaurant();

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());

        MenuDTO mappedMenu = menuMapper.mapEntityToDTO(menu);

        assertNotNull(mappedMenu);
        assertThat(menuDTOComparator.compare(menuDTO, mappedMenu)).isZero();
        assertArrayEquals(menuDTO.getContent(), mappedMenu.getContent());
    }

    @Test
    public void whenGivenMenuWithNullValues_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildNewRestaurant();

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());
        menu.setContent(null);
        menu.setContentType(null);

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());
        menuDTO.setContent(null);
        menuDTO.setContentType(null);

        MenuDTO mappedMenu = menuMapper.mapEntityToDTO(menu);

        assertNotNull(mappedMenu);
        assertNull(mappedMenu.getContent());
        assertNull(mappedMenu.getContentType());
        assertEquals(menuDTO.getId(), mappedMenu.getId());
        assertEquals(menuDTO.getFileName(), mappedMenu.getFileName());
    }

    @Test
    public void whenGivenNullMenu_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> menuMapper.mapEntityToDTO(null));
    }

    @Test
    public void whenGivenMenuDTO_returnEntityWithSameFieldValues() {
        Restaurant restaurant = saveNewRestaurant();

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(restaurant.getId());

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setId(restaurant.getId());

        Menu mappedMenuDTO = menuMapper.mapDTOtoEntity(menuDTO);

        assertNotNull(mappedMenuDTO);
        assertThat(menuComparator.compare(menu, mappedMenuDTO)).isZero();
        assertArrayEquals(menu.getContent(), mappedMenuDTO.getContent());
        assertEquals(menu.getRestaurant(), mappedMenuDTO.getRestaurant());
    }

    @Test
    public void whenGivenMenuDTOWithNullValues_returnEntityWithSameFieldValues() {
        Restaurant restaurant = saveNewRestaurant();

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setContent(null);
        menu.setContentType(null);
        menu.setId(restaurant.getId());

        MenuDTO menuDTO = TestDataBuilder.buildMenuDTO();
        assert menuDTO != null;
        menuDTO.setContent(null);
        menuDTO.setContentType(null);
        menuDTO.setId(restaurant.getId());

        Menu mappedMenuDTO = menuMapper.mapDTOtoEntity(menuDTO);

        assertNotNull(mappedMenuDTO);
        assertNull(mappedMenuDTO.getContent());
        assertNull(mappedMenuDTO.getContentType());
        assertEquals(menu.getId(), mappedMenuDTO.getId());
        assertEquals(menu.getFileName(), mappedMenuDTO.getFileName());
    }

    @Test
    public void whenGivenNullMenuDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> menuMapper.mapDTOtoEntity(null));
    }

    @NotNull
    private Restaurant saveNewRestaurant() {
        return restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());
    }

    @NotNull
    private Restaurant buildNewRestaurant() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));
        return restaurant;
    }
}
