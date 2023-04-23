package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.exception.NotEnoughCapacityAndTablesException;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.BookingMapper;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import com.nagarro.af.bookingtablesystem.repository.BookingRepository;
import com.nagarro.af.bookingtablesystem.service.BookingService;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final CustomerService customerService;

    private final RestaurantService restaurantService;

    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, CustomerService customerService, RestaurantService restaurantService, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDTO makeBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.mapDTOtoEntity(bookingDTO);
        booking.updateRestaurantCapacity();
        booking.addBookingToCustomer();
        booking.addBookingToRestaurant();

        if (isEnoughRestaurantCapacity(booking)) {
            return bookingMapper.mapEntityToDTO(bookingRepository.makeBooking(booking));
        }

        throw new NotEnoughCapacityAndTablesException();
    }

    @Override
    public BookingDTO findById(UUID id) {
        return bookingRepository.findById(id)
                .map(this::mapToBookingDTO)
                .orElseThrow(() -> new NotFoundException("Booking with id " + id +
                        " could not be found!"));
    }

    @Override
    public List<BookingDTO> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookingMapper.mapEntityListToDTOList(bookings);
    }

    @Override
    public List<BookingDTO> findAllByCustomerId(UUID id) {
        customerService.findById(id);
        List<Booking> bookings = bookingRepository.findAllByCustomerId(id);
        return bookingMapper.mapEntityListToDTOList(bookings);
    }

    @Override
    public List<BookingDTO> findAllByRestaurantId(UUID id) {
        restaurantService.findById(id);
        List<Booking> bookings = bookingRepository.findAllByRestaurantId(id);
        return bookingMapper.mapEntityListToDTOList(bookings);
    }

    @Override
    public void delete(UUID id) {
        bookingRepository.deleteById(id);
    }

    private boolean isEnoughRestaurantCapacity(Booking booking) {
        int tablesNo = booking.getTablesNo();
        int customersNo = booking.getCustomersNo();

        RestaurantCapacity availableCapacity = booking.getRestaurant()
                .getDateCapacityAvailability()
                .get(booking.getBookingDate());

        return (availableCapacity.getTablesNo() >= tablesNo) && (availableCapacity.getCustomersNo() >= customersNo);
    }

    private BookingDTO mapToBookingDTO(Booking booking) {
        return bookingMapper.mapEntityToDTO(booking);
    }
}
