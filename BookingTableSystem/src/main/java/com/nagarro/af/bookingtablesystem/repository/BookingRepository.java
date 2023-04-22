package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, CustomBookingRepository {
    List<Booking> findAllByCustomerId(UUID id);

    List<Booking> findAllByRestaurantId(UUID id);
}
