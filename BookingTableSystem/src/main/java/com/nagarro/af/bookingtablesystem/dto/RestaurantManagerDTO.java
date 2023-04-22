package com.nagarro.af.bookingtablesystem.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantManagerDTO extends UserDTO {
    private List<UUID> restaurantIds = new ArrayList<>();

    public RestaurantManagerDTO() {
    }

    public RestaurantManagerDTO(String username, String password, String fullName, String email, String phoneNo,
                                String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }

    public List<UUID> getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(List<UUID> restaurantIds) {
        this.restaurantIds = restaurantIds;
    }

    public void addRestaurantId(UUID id) {
        restaurantIds.add(id);
    }
}
