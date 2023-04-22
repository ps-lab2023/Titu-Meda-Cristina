package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ITRestaurantRepository extends ITBaseRepository {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void testSave_OneToOneRelationship_Menu() {
        Restaurant restaurant = buildRestaurantWithMenu();

        Restaurant returnedRestaurant = restaurantRepository.save(restaurant);

        assertTrue(restaurantRepository.findById(returnedRestaurant.getId()).isPresent());
        assertTrue(menuRepository.findById(returnedRestaurant.getId()).isPresent());
    }

    @Test
    public void testDelete_OneToOneRelationship_Menu() {
        Restaurant restaurant = buildRestaurantWithMenu();

        Restaurant returnedRestaurant = restaurantRepository.saveAndFlush(restaurant);

        restaurantRepository.delete(returnedRestaurant);
        assertTrue(restaurantRepository.findById(returnedRestaurant.getId()).isEmpty());
        assertTrue(menuRepository.findById(returnedRestaurant.getId()).isEmpty());
    }

    @Test
    public void testFindByName_success() {
        Restaurant restaurant = saveNewRestaurant();
        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());

        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(restaurant.getName());
        assertTrue(restaurantOptional.isPresent());
    }

    @Test
    public void testFindByName_notFound() {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName("Nothing");
        assertTrue(restaurantOptional.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_success() {
        Restaurant restaurantOne = saveNewRestaurant();

        String country = restaurantOne.getCountry();
        String city = restaurantOne.getCity();

        Restaurant restaurantTwo = restaurantRepository.saveAndFlush(
                new Restaurant(
                        "Soho",
                        "soho@yahoo.com",
                        "+40789251667",
                        country,
                        city,
                        "Best american food in town!",
                        "str. Copacei 34",
                        null,
                        100,
                        30)
        );

        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity(country, city);
        assertFalse(restaurants.isEmpty());
        assertEquals(restaurantOne, restaurants.get(0));
        assertEquals(restaurantTwo, restaurants.get(1));
    }

    @Test
    public void testFindAllByCountryAndCity_emptyList() {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity("Bulgaria", "Sofia");
        assertTrue(restaurants.isEmpty());
    }

    @NotNull
    private Restaurant buildRestaurantWithMenu() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        Menu menu = TestDataBuilder.buildMenu(restaurant);
        restaurant.setMenu(menu);
        return restaurant;
    }

    @NotNull
    private Restaurant saveNewRestaurant() {
        return restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());
    }
}