package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.repository.BookingRepository;
import com.nagarro.af.bookingtablesystem.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO makeBooking(BookingDTO bookingDTO) {
        return null;
    }

    @Override
    public Optional<BookingDTO> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<BookingDTO> findAll() {
        return null;
    }

    @Override
    public List<BookingDTO> findAllByCustomerId(UUID id) {
        return null;
    }

    @Override
    public List<BookingDTO> findAllByRestaurantId(UUID id) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
