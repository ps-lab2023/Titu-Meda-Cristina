package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantCapacityRepository extends JpaRepository<RestaurantCapacity, UUID> {
}
