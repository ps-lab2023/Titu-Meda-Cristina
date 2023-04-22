package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingDTO makeBooking(BookingDTO bookingDTO);

    BookingDTO findById(UUID id);

    List<BookingDTO> findAll();

    List<BookingDTO> findAllByCustomerId(UUID id);

    List<BookingDTO> findAllByRestaurantId(UUID id);

    void delete(UUID id);
}
