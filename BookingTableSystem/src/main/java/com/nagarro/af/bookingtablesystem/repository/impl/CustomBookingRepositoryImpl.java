package com.nagarro.af.bookingtablesystem.repository.impl;

import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import com.nagarro.af.bookingtablesystem.repository.CustomBookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

public class CustomBookingRepositoryImpl implements CustomBookingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomBookingRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Booking makeBooking(Booking booking) {
        int tablesNo = booking.getTablesNo();
        int customersNo = booking.getCustomersNo();
        Date bookingDate = booking.getBookingDate();

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
    }

    private RestaurantCapacity updateRestaurantCapacity(RestaurantCapacity restaurantCapacity, int customersNo, int tablesNo) {
        restaurantCapacity.setCustomersNo(customersNo);
        restaurantCapacity.setTablesNo(tablesNo);
        return restaurantCapacity;
    }
}
