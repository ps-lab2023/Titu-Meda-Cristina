package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.*;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ITBookingRepository extends ITBaseRepository {

    private static final UUID INVALID_ID = UUID.fromString("fe1e83bd-70a6-4ffa-818d-05a4f1b4bcab");

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;

    @Autowired
    private RestaurantCapacityRepository restaurantCapacityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testMakeBooking_success() {
        Booking booking = createBooking();
        int bookingCustomersNo = booking.getCustomersNo();
        int bookingTablesNo = booking.getTablesNo();

        RestaurantCapacity initialCapacity = getRestaurantCapacity(booking);
        int initialCustomersNo = initialCapacity.getCustomersNo();
        int initialTablesNo = initialCapacity.getTablesNo();

        booking = bookingRepository.makeBooking(booking);

        RestaurantCapacity updatedCapacity = findRestaurantCapacity(booking);

        int updatedCustomersNo = updatedCapacity.getCustomersNo();
        int updatedTablesNo = updatedCapacity.getTablesNo();

        assertEquals(initialCustomersNo - bookingCustomersNo, updatedCustomersNo);
        assertEquals(initialTablesNo - bookingTablesNo, updatedTablesNo);
    }

    @Test
    public void testFindAllByCustomerId_success() {
        Booking bookingOne = createBooking();

        Restaurant restaurant = new Restaurant("Klausen", "klausen@yahoo.com", "+40766998102",
                "Romania", "Bucuresti", "Calea Victoriei 17", "Best food in the capital!",
                null, 100, 35);

        Customer customer = bookingOne.getCustomer();
        RestaurantManager restaurantManager = bookingOne.getRestaurant().getRestaurantManager();

        restaurantManager.addRestaurant(restaurant);
        restaurantManagerRepository.save(restaurantManager);

        restaurant = restaurantRepository.saveAndFlush(restaurant);

        Booking bookingTwo = TestDataBuilder.buildBooking(restaurant, customer);

        bookingOne = bookingRepository.saveAndFlush(bookingOne);
        bookingTwo = bookingRepository.saveAndFlush(bookingTwo);

        List<Booking> bookings = bookingRepository.findAllByCustomerId(customer.getId());
        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0), bookingOne);
        assertEquals(bookings.get(1), bookingTwo);
    }

    @Test
    public void testFindAllByCustomerId_emptyList() {
        bookingRepository.saveAndFlush(createBooking());

        List<Booking> bookings = bookingRepository.findAllByCustomerId(INVALID_ID);
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testFindAllByRestaurantId_success() {
        Booking booking = bookingRepository.saveAndFlush(createBooking());

        List<Booking> bookings = bookingRepository.findAllByRestaurantId(booking.getRestaurant().getId());
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.contains(booking));
    }

    @Test
    public void testFindAllByRestaurantId_emptyList() {
        bookingRepository.saveAndFlush(createBooking());

        List<Booking> bookings = bookingRepository.findAllByRestaurantId(INVALID_ID);
        assertTrue(bookings.isEmpty());
    }

    @NotNull
    private Booking createBooking() {
        Customer customer = saveNewCustomer();

        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);

        restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);

        return TestDataBuilder.buildBooking(restaurant, customer);
    }

    private RestaurantCapacity getRestaurantCapacity(Booking booking) {
        return booking.getRestaurant().getDateCapacityAvailability().get(booking.getBookingDate());
    }

    private RestaurantCapacity findRestaurantCapacity(Booking booking) {
        Optional<RestaurantCapacity> restaurantCapacityOptional = restaurantCapacityRepository.findById(
                getRestaurantCapacity(booking).getId()
        );

        return restaurantCapacityOptional.orElse(null);
    }

    @NotNull
    private Customer saveNewCustomer() {
        return customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());
    }
}
