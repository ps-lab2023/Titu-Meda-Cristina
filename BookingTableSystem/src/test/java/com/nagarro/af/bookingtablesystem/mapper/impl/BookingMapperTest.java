package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.mapper.MapperTest;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest extends MapperTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenGivenBooking_thenReturnDTOWithSameFieldValues() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        Customer customer = TestDataBuilder.buildCustomer();
        customer.setId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));

        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);
        booking.setId(UUID.fromString(TestDataBuilder.BOOKING_ID));

        BookingDTO bookingDTO = TestDataBuilder.buildBookingDTO(restaurant.getId(), customer.getId());
        bookingDTO.setId(booking.getId());

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
        Restaurant restaurant = restaurantRepository.saveAndFlush(TestDataBuilder.buildRestaurant());

        Customer customer = customerRepository.saveAndFlush(TestDataBuilder.buildCustomer());

        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);
        booking.setId(UUID.fromString(TestDataBuilder.BOOKING_ID));

        BookingDTO bookingDTO = TestDataBuilder.buildBookingDTO(restaurant.getId(), customer.getId());
        bookingDTO.setId(booking.getId());

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
}
