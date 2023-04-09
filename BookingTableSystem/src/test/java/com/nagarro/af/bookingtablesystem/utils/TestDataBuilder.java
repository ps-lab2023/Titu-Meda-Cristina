package com.nagarro.af.bookingtablesystem.utils;

import com.nagarro.af.bookingtablesystem.dto.*;
import com.nagarro.af.bookingtablesystem.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataBuilder {
    public static final String RESTAURANT_ID = "3f81c2f1-2225-4cbe-9be9-59e5f2fe426a";
    public static final String RESTAURANT_CAPACITY_ID = "3f81c2f1-2225-4cbe-9be9-59e5f2fe426a";
    public static final String CUSTOMER_ID = "d0cb12ab-a96b-4b63-bf29-3bedbee80d55";
    public static final String RESTAURANT_MANAGER_ID = "b1810386-ed01-4126-be40-e5b8d46cb763";
    public static final String BOOKING_ID = "663cbc8f-3380-4417-bbf2-769fe401dcaf";

    public static Menu buildMenu(Restaurant restaurant) {
        Path path = Paths.get("src/main/resources/menu/Menu.jpg");
        try {
            byte[] content = java.nio.file.Files.readAllBytes(path);
            return new Menu(
                    "menu/Menu.jpg",
                    content,
                    "image/jpeg",
                    restaurant
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MenuDTO buildMenuDTO() {
        Path path = Paths.get("src/main/resources/menu/Menu.jpg");
        try {
            byte[] content = java.nio.file.Files.readAllBytes(path);
            return new MenuDTO(
                    "menu/Menu.jpg",
                    content,
                    "image/jpeg"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Restaurant buildRestaurant() {
        return new Restaurant(
                "Garlic",
                "garlic@yahoo.com",
                "+40766578102",
                "Romania",
                "Brasov",
                "str. Lazar 18",
                "No vampires for sure!",
                null,
                50,
                15
        );
    }

    public static RestaurantDTO buildRestaurantDTO() {
        return new RestaurantDTO(
                "Garlic",
                "garlic@yahoo.com",
                "+40766578102",
                "Romania",
                "Brasov",
                "str. Lazar 18",
                "No vampires for sure!",
                50,
                15
        );
    }

    public static Customer buildCustomer() {
        return new Customer(
                "customerone",
                "Test123!",
                "Customer One",
                "customer_one@yahoo.com",
                "+40765124990",
                "Romania",
                "Brasov"
        );
    }

    public static CustomerDTO buildCustomerDTO() {
        return new CustomerDTO(
                "customerone",
                "Test123!",
                "Customer One",
                "customer_one@yahoo.com",
                "+40765124990",
                "Romania",
                "Brasov"
        );
    }

    public static RestaurantManager buildRestaurantManager() {
        return new RestaurantManager(
                "manager-garlic",
                "Test123!",
                "Manager Garlic",
                "manager_garlic@yahoo.com",
                "+40789678123",
                "Romania",
                "Brasov"
        );
    }

    public static RestaurantManagerDTO buildRestaurantManagerDTO() {
        return new RestaurantManagerDTO(
                "manager-garlic",
                "Test123!",
                "Manager Garlic",
                "manager_garlic@yahoo.com",
                "+40789678123",
                "Romania",
                "Brasov"
        );
    }

    public static Booking buildBooking(Restaurant restaurant, Customer customer) {
        return new Booking(
                customer,
                restaurant,
                LocalDateTime.of(2023, 12, 13, 15, 0),
                3,
                1
        );
    }

    public static BookingDTO buildBookingDTO(UUID restaurantId, UUID customerId) {
        return new BookingDTO(
                customerId,
                restaurantId,
                LocalDateTime.of(2023, 12, 13, 15, 0),
                3,
                1
        );
    }
}
