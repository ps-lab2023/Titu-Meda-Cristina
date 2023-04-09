package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.*;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ITBookingRepository extends ITRepository {

    private static final String UNUSED_ID = "fe1e83bd-70a6-4ffa-818d-05a4f1b4bcab";

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
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());

        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);
        int bookingCustomersNo = booking.getCustomersNo();
        int bookingTablesNo = booking.getTablesNo();

        RestaurantCapacity capacity = booking.getRestaurant().getDateCapacityAvailability().get(booking.getBookingDate());
        int initialCustomersNo = capacity.getCustomersNo();
        int initialTablesNo = capacity.getTablesNo();

        booking = bookingRepository.makeBooking(booking);
        assertTrue(bookingRepository.findById(booking.getId()).isPresent());

        Optional<RestaurantCapacity> restaurantCapacityOptional = restaurantCapacityRepository.findById(
                booking.getRestaurant().getDateCapacityAvailability().get(booking.getBookingDate()).getId()
        );
        assertTrue(restaurantCapacityOptional.isPresent());

        int updatedCustomersNo = restaurantCapacityOptional.get().getCustomersNo();
        int updatedTablesNo = restaurantCapacityOptional.get().getTablesNo();

        assertEquals(initialCustomersNo - bookingCustomersNo, updatedCustomersNo);
        assertEquals(initialTablesNo - bookingTablesNo, updatedTablesNo);
    }

    @Test
    public void testFindAllByCustomerId_success() {
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Restaurant restaurantOne = TestDataBuilder.buildRestaurant();
        Restaurant restaurantTwo = new Restaurant("Klausen", "klausen@yahoo.com", "+40766998102",
                "Romania", "Bucuresti", "Calea Victoriei 17", "Best food in the capital!",
                null, 100, 35);

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurantOne);
        restaurantManager.addRestaurant(restaurantTwo);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurantOne = restaurantRepository.saveAndFlush(restaurantOne);
        restaurantTwo = restaurantRepository.saveAndFlush(restaurantTwo);

        assertTrue(restaurantRepository.findById(restaurantOne.getId()).isPresent());
        assertTrue(restaurantRepository.findById(restaurantTwo.getId()).isPresent());
        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());

        Booking bookingOne = TestDataBuilder.buildBooking(restaurantOne, customer);
        Booking bookingTwo = TestDataBuilder.buildBooking(restaurantTwo, customer);

        bookingOne = bookingRepository.saveAndFlush(bookingOne);
        bookingTwo = bookingRepository.saveAndFlush(bookingTwo);

        assertTrue(bookingRepository.findById(bookingOne.getId()).isPresent());
        assertTrue(bookingRepository.findById(bookingTwo.getId()).isPresent());

        List<Booking> bookings = bookingRepository.findAllByCustomerId(customer.getId());
        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0), bookingOne);
        assertEquals(bookings.get(1), bookingTwo);
    }

    @Test
    public void testFindAllByCustomerId_emptyList() {
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());

        Booking booking = bookingRepository.saveAndFlush(TestDataBuilder.buildBooking(restaurant, customer));
        assertTrue(bookingRepository.findById(booking.getId()).isPresent());

        List<Booking> bookings = bookingRepository.findAllByCustomerId(UUID.fromString(UNUSED_ID));
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testFindAllByRestaurantId_success() {
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());

        Booking booking = bookingRepository.saveAndFlush(TestDataBuilder.buildBooking(restaurant, customer));
        assertTrue(bookingRepository.findById(booking.getId()).isPresent());

        List<Booking> bookings = bookingRepository.findAllByRestaurantId(restaurant.getId());
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.contains(booking));
    }

    @Test
    public void testFindAllByRestaurantId_emptyList() {
        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Restaurant restaurant = TestDataBuilder.buildRestaurant();

        RestaurantManager restaurantManager = TestDataBuilder.buildRestaurantManager();
        restaurantManager.addRestaurant(restaurant);

        restaurantManager = restaurantManagerRepository.saveAndFlush(restaurantManager);
        restaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
        assertTrue(restaurantManagerRepository.findById(restaurantManager.getId()).isPresent());

        Booking booking = bookingRepository.saveAndFlush(TestDataBuilder.buildBooking(restaurant, customer));
        assertTrue(bookingRepository.findById(booking.getId()).isPresent());

        List<Booking> bookings = bookingRepository.findAllByRestaurantId(UUID.fromString(UNUSED_ID));
        assertTrue(bookings.isEmpty());
    }
}
