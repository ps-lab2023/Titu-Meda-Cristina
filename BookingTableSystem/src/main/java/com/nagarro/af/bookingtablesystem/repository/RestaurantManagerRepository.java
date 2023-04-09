package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantManagerRepository extends UserRepository<RestaurantManager, UUID> {
}
