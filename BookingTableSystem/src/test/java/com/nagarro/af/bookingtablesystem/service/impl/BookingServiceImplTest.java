package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.exception.NotEnoughCapacityAndTablesException;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.BookingMapper;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import com.nagarro.af.bookingtablesystem.repository.BookingRepository;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    private static final UUID RESTAURANT_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_ID);

    private static final UUID CUSTOMER_UUID_TEST = UUID.fromString(TestDataBuilder.CUSTOMER_ID);

    private static final UUID BOOKING_UUID_TEST = UUID.fromString(TestDataBuilder.BOOKING_ID);

    private static final Booking BOOKING_TEST = TestDataBuilder.buildBooking(TestDataBuilder.buildRestaurant(),
            TestDataBuilder.buildCustomer());

    private static final BookingDTO BOOKING_DTO_TEST = TestDataBuilder.buildBookingDTO(
            RESTAURANT_UUID_TEST,
            CUSTOMER_UUID_TEST
    );

    private static final Comparator<BookingDTO> BOOKING_DTO_COMPARATOR = TestComparators.BOOKING_DTO_COMPARATOR;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    public void testMakeBooking_returnSavedBooking() {
        BOOKING_TEST.setId(BOOKING_UUID_TEST);
        BOOKING_DTO_TEST.setId(BOOKING_UUID_TEST);

        when(bookingMapper.mapDTOtoEntity(BOOKING_DTO_TEST)).thenReturn(BOOKING_TEST);
        when(bookingRepository.makeBooking(BOOKING_TEST)).thenReturn(BOOKING_TEST);
        when(bookingMapper.mapEntityToDTO(BOOKING_TEST)).thenReturn(BOOKING_DTO_TEST);

        BookingDTO returnedBookingDTO = bookingService.makeBooking(BOOKING_DTO_TEST);

        verify(bookingRepository).makeBooking(bookingArgumentCaptor.capture());
        assertNotNull(returnedBookingDTO);
        assertThat(BOOKING_DTO_COMPARATOR.compare(returnedBookingDTO, BOOKING_DTO_TEST)).isZero();
    }

    @Test
    public void testMakeBooking_throwNotEnoughCapacityAndTablesException() {
        BOOKING_TEST.getRestaurant()
                .getDateCapacityAvailability()
                .put(BOOKING_TEST.getBookingDate(), new RestaurantCapacity(0, 0));

        when(bookingMapper.mapDTOtoEntity(BOOKING_DTO_TEST)).thenReturn(BOOKING_TEST);

        assertThrows(NotEnoughCapacityAndTablesException.class, () -> bookingService.makeBooking(BOOKING_DTO_TEST));
    }

    @Test
    public void testFindById_success() {
        BOOKING_TEST.setId(BOOKING_UUID_TEST);
        BOOKING_DTO_TEST.setId(BOOKING_UUID_TEST);

        when(bookingRepository.findById(BOOKING_UUID_TEST)).thenReturn(Optional.of(BOOKING_TEST));
        when(bookingMapper.mapEntityToDTO(BOOKING_TEST)).thenReturn(BOOKING_DTO_TEST);

        BookingDTO returnedBookingDTO = bookingService.findById(BOOKING_UUID_TEST);

        assertNotNull(returnedBookingDTO);
        assertThat(BOOKING_DTO_COMPARATOR.compare(returnedBookingDTO, BOOKING_DTO_TEST)).isZero();
    }

    @Test
    public void testFindById_notFound() {
        when(bookingRepository.findById(BOOKING_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.findById(BOOKING_UUID_TEST));
    }

    @Test
    public void testFindAll_success() {
        BOOKING_TEST.setId(BOOKING_UUID_TEST);
        BOOKING_DTO_TEST.setId(BOOKING_UUID_TEST);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(BOOKING_TEST);

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        bookingDTOS.add(BOOKING_DTO_TEST);

        when(bookingRepository.findAll()).thenReturn(bookings);
        when(bookingMapper.mapEntityListToDTOList(bookings)).thenReturn(bookingDTOS);

        List<BookingDTO> returnedBookingDTOList = bookingService.findAll();

        assertNotNull(returnedBookingDTOList);
        assertTrue(returnedBookingDTOList.containsAll(bookingDTOS));
    }

    @Test
    public void testFindAll_returnEmptyList() {
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());

        List<BookingDTO> returnedBookingDTOList = bookingService.findAll();

        assertTrue(returnedBookingDTOList.isEmpty());
    }

    @Test
    public void testFindAllByCustomerId_success() {
        BOOKING_TEST.setId(BOOKING_UUID_TEST);
        BOOKING_DTO_TEST.setId(BOOKING_UUID_TEST);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(BOOKING_TEST);

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        bookingDTOS.add(BOOKING_DTO_TEST);

        when(bookingRepository.findAllByCustomerId(CUSTOMER_UUID_TEST)).thenReturn(bookings);
        when(bookingMapper.mapEntityListToDTOList(bookings)).thenReturn(bookingDTOS);

        List<BookingDTO> returnedBookingDTOList = bookingService.findAllByCustomerId(CUSTOMER_UUID_TEST);

        assertNotNull(returnedBookingDTOList);
        assertTrue(returnedBookingDTOList.containsAll(bookingDTOS));
    }

    @Test
    public void testFindAllByCustomerId_returnEmptyList() {
        when(bookingRepository.findAllByCustomerId(CUSTOMER_UUID_TEST)).thenReturn(new ArrayList<>());

        List<BookingDTO> returnedBookingDTOList = bookingService.findAllByCustomerId(CUSTOMER_UUID_TEST);

        assertTrue(returnedBookingDTOList.isEmpty());
    }

    @Test
    public void testFindAllByRestaurantId_success() {
        BOOKING_TEST.setId(BOOKING_UUID_TEST);
        BOOKING_DTO_TEST.setId(BOOKING_UUID_TEST);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(BOOKING_TEST);

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        bookingDTOS.add(BOOKING_DTO_TEST);

        when(bookingRepository.findAllByRestaurantId(RESTAURANT_UUID_TEST)).thenReturn(bookings);
        when(bookingMapper.mapEntityListToDTOList(bookings)).thenReturn(bookingDTOS);

        List<BookingDTO> returnedBookingDTOList = bookingService.findAllByRestaurantId(RESTAURANT_UUID_TEST);

        assertNotNull(returnedBookingDTOList);
        assertTrue(returnedBookingDTOList.containsAll(bookingDTOS));
    }

    @Test
    public void testFindAllByRestaurantId_returnEmptyList() {
        when(bookingRepository.findAllByRestaurantId(RESTAURANT_UUID_TEST)).thenReturn(new ArrayList<>());

        List<BookingDTO> returnedBookingDTOList = bookingService.findAllByRestaurantId(RESTAURANT_UUID_TEST);

        assertTrue(returnedBookingDTOList.isEmpty());
    }
}
