package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql({"classpath:scripts/insert_restaurant_managers.sql", "classpath:scripts/insert_restaurants.sql"})
public class ITRestaurantRepository extends ITRepository {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void testSave_OneToOneRelationship() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        Menu menu = TestDataBuilder.buildMenu(restaurant);
        restaurant.setMenu(menu);

        Restaurant returnedRestaurant = restaurantRepository.save(restaurant);

        assertTrue(restaurantRepository.findById(returnedRestaurant.getId()).isPresent());
        assertTrue(menuRepository.findById(returnedRestaurant.getId()).isPresent());
    }

    @Test
    public void testFindByName_success() {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName("Meat Up");
        assertTrue(restaurantOptional.isPresent());
    }

    @Test
    public void testFindByName_notFound() {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName("Nothing");
        assertTrue(restaurantOptional.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_success() {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity("Romania", "Cluj-Napoca");
        assertFalse(restaurants.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_emptyList() {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity("Bulgaria", "Sofia");
        assertTrue(restaurants.isEmpty());
    }
}