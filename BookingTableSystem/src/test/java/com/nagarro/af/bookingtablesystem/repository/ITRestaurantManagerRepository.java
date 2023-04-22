package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ITRestaurantManagerRepository extends ITBaseRepository {

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;

    @Test
    public void testFindByEmail_success() {
        RestaurantManager manager = restaurantManagerRepository.saveAndFlush(TestDataBuilder.buildRestaurantManager());

        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByEmail(manager.getEmail());
        assertTrue(optionalRestaurantManager.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByEmail("nothing");
        assertTrue(optionalRestaurantManager.isEmpty());
    }
}
