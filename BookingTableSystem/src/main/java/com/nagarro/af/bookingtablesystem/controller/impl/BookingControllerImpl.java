package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.BookingController;
import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class BookingControllerImpl implements BookingController {

    private final BookingService bookingService;

    public BookingControllerImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<BookingDTO> makeBooking(BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.makeBooking(bookingDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDTO> findById(UUID id) {
        return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDTO>> findAll() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDTO>> findAllByCustomerId(UUID customerId) {
        return new ResponseEntity<>(bookingService.findAllByCustomerId(customerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDTO>> findAllByRestaurantId(UUID restaurantId) {
        return new ResponseEntity<>(bookingService.findAllByRestaurantId(restaurantId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
