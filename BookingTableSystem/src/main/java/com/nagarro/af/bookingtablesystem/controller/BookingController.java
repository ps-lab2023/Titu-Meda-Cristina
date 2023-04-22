package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/bookings")
public interface BookingController {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Save a new booking.",
            response = BookingDTO.class,
            notes = "Return the created booking.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking successfully created!"),
            @ApiResponse(code = 400, message = "Bad request!")
    })
    ResponseEntity<BookingDTO> makeBooking(@Valid @RequestBody BookingDTO bookingDTO);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find booking by id.",
            response = BookingDTO.class,
            notes = "Return the booking with the given id if found.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Booking found!"),
            @ApiResponse(code = 404, message = "Booking not found!")
    })
    ResponseEntity<BookingDTO> findById(@PathVariable UUID id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all Bookings.",
            notes = "Return all the saved Bookings.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bookings returned!")
    })
    ResponseEntity<List<BookingDTO>> findAll();

    @GetMapping(
            path = "/customer",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all bookings made by a customer.",
            notes = "Return all the reservations made by that customer.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bookings returned!")
    })
    ResponseEntity<List<BookingDTO>> findAllByCustomerId(@RequestParam("id") UUID customerId);

    @GetMapping(
            path = "/restaurant",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find all bookings for a specific restaurant.",
            notes = "Return all the reservations made for that restaurant.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bookings returned!")
    })
    ResponseEntity<List<BookingDTO>> findAllByRestaurantId(@RequestParam("id") UUID restaurantId);

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an existing booking by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Booking successfully deleted!"),
            @ApiResponse(code = 404, message = "Booking not found!")
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
