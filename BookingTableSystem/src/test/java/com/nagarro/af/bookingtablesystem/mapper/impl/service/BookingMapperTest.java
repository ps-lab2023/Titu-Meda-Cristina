package com.nagarro.af.bookingtablesystem.mapper.impl.service;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperBaseTest;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest extends MapperBaseTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenGivenBooking_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = buildRestaurant();

        Customer customer = buildCustomer();

        Booking booking = buildBooking(restaurant, customer);

        BookingDTO bookingDTO = buildBookingDTO(restaurant, customer);

        BookingDTO mappedBooking = bookingMapper.mapEntityToDTO(booking);

        assertNotNull(mappedBooking);
        assertThat(bookingDTOComparator.compare(bookingDTO, mappedBooking)).isZero();
    }

    @Test
    public void whenGivenNullBooking_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookingMapper.mapEntityToDTO(null));
    }

    @Test
    public void whenGivenBookingDTO_thenReturnEntityWithSameFieldValues() {
        Restaurant restaurant = saveNewRestaurant();

        Customer customer = saveNewCustomer();

        Booking booking = buildBooking(restaurant, customer);

        BookingDTO bookingDTO = buildBookingDTO(restaurant, customer);

        Booking mappedBookingDTO = bookingMapper.mapDTOtoEntity(bookingDTO);
        Restaurant mappedRestaurantDTO = mappedBookingDTO.getRestaurant();
        Customer mappedCustomerDTO = mappedBookingDTO.getCustomer();

        assertNotNull(mappedRestaurantDTO);
        assertNull(mappedRestaurantDTO.getMenu());
        assertNull(mappedRestaurantDTO.getRestaurantManager());
        assertThat(restaurantComparator.compare(restaurant, mappedRestaurantDTO)).isZero();
        assertThat(mappedRestaurantDTO.getDateCapacityAvailability().size()).isOne();

        assertNotNull(mappedCustomerDTO);
        assertThat(userComparator.compare(customer, mappedCustomerDTO)).isZero();

        assertNotNull(mappedBookingDTO);
        assertThat(bookingComparator.compare(booking, mappedBookingDTO)).isZero();
    }

    @Test
    public void whenGivenNullBookingDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookingMapper.mapDTOtoEntity(null));
    }

    @NotNull
    private Customer buildCustomer() {
        Customer customer = TestDataBuilder.buildCustomer();
        customer.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));
        return customer;
    }

    @NotNull
    private Restaurant buildRestaurant() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));
        return restaurant;
    }

    @NotNull
    private Booking buildBooking(Restaurant restaurant, Customer customer) {
        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);
        booking.setId(UUID.fromString(TestDataBuilder.BOOKING_ID));
        return booking;
    }

    @NotNull
    private BookingDTO buildBookingDTO(Restaurant restaurant, Customer customer) {
        BookingDTO bookingDTO = TestDataBuilder.buildBookingDTO(restaurant.getId(), customer.getId());
        bookingDTO.setId(UUID.fromString(TestDataBuilder.BOOKING_ID));
        return bookingDTO;
    }

    @NotNull
    private Customer saveNewCustomer() {
        return customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());
    }

    @NotNull
    private Restaurant saveNewRestaurant() {
        return restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());
    }
}
