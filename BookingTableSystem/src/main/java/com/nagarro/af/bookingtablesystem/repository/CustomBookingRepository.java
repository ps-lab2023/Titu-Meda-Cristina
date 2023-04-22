package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Booking;

public interface CustomBookingRepository {
    Booking makeBooking(Booking booking);
}
