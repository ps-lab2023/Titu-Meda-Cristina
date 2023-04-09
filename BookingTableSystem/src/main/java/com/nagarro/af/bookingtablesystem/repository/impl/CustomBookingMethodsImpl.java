package com.nagarro.af.bookingtablesystem.repository.impl;

import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import com.nagarro.af.bookingtablesystem.repository.CustomBookingMethods;
import com.nagarro.af.bookingtablesystem.repository.exception.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CustomBookingMethodsImpl implements CustomBookingMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomBookingMethodsImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Booking makeBooking(Booking booking) {
        Restaurant restaurant = booking.getRestaurant();
        int tablesNo = booking.getTablesNo();
        int customersNo = booking.getCustomersNo();
        Date bookingDate = booking.getBookingDate();
        RestaurantCapacity availableCapacity = restaurant.getDateCapacityAvailability().get(bookingDate);

        if (isEnoughRestaurantCapacity(availableCapacity, tablesNo, customersNo)) {
            entityManager.persist(booking);

            RestaurantCapacity capacity = booking.getRestaurant().getDateCapacityAvailability().get(bookingDate);
            int remainingCustomersNo = capacity.getCustomersNo() - customersNo;
            int remainingTablesNo = capacity.getTablesNo() - tablesNo;
            entityManager.persist(
                    updateRestaurantCapacity(capacity, remainingCustomersNo, remainingTablesNo)
            );

            entityManager.flush();
            LOGGER.info("New booking with id " + booking.getId() + " has been saved.");
            return booking;
        } else {
            throw new RepositoryException("Not enough tables or capacity at the restaurant!");
        }
    }

    private boolean isEnoughRestaurantCapacity(RestaurantCapacity restaurantCapacity, int tablesNo, int customersNo) {
        return (restaurantCapacity.getTablesNo() >= tablesNo) && (restaurantCapacity.getCustomersNo() >= customersNo);
    }

    private RestaurantCapacity updateRestaurantCapacity(RestaurantCapacity restaurantCapacity, int customersNo, int tablesNo) {
        restaurantCapacity.setCustomersNo(customersNo);
        restaurantCapacity.setTablesNo(tablesNo);
        return restaurantCapacity;
    }
}
