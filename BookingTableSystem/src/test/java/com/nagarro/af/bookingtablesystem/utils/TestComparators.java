package com.nagarro.af.bookingtablesystem.utils;

import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.User;

import java.util.Comparator;

public class TestComparators {

    public static final Comparator<Menu> MENU_COMPARATOR = Comparator.comparing(Menu::getId)
            .thenComparing(Menu::getFileName)
            .thenComparing(Menu::getContentType);

    public static final Comparator<MenuDTO> MENU_DTO_COMPARATOR = Comparator.comparing(MenuDTO::getId)
            .thenComparing(MenuDTO::getFileName)
            .thenComparing(MenuDTO::getContentType);

    public static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getId)
            .thenComparing(User::getUsername)
            .thenComparing(User::getPassword)
            .thenComparing(User::getFullName)
            .thenComparing(User::getEmail)
            .thenComparing(User::getPhoneNo)
            .thenComparing(User::getCountry)
            .thenComparing(User::getCity);

    public static final Comparator<UserDTO> USER_DTO_COMPARATOR = Comparator.comparing(UserDTO::getId)
            .thenComparing(UserDTO::getUsername)
            .thenComparing(UserDTO::getPassword)
            .thenComparing(UserDTO::getFullName)
            .thenComparing(UserDTO::getEmail)
            .thenComparing(UserDTO::getPhoneNo)
            .thenComparing(UserDTO::getCountry)
            .thenComparing(UserDTO::getCity);

    public static final Comparator<Restaurant> RESTAURANT_COMPARATOR = Comparator.comparing(Restaurant::getId)
            .thenComparing(Restaurant::getName)
            .thenComparing(Restaurant::getEmail)
            .thenComparing(Restaurant::getPhoneNo)
            .thenComparing(Restaurant::getCountry)
            .thenComparing(Restaurant::getCity)
            .thenComparing(Restaurant::getAddress)
            .thenComparing(Restaurant::getDescription)
            .thenComparing(Restaurant::getMaxCustomersNo)
            .thenComparing(Restaurant::getMaxTablesNo);

    public static final Comparator<RestaurantDTO> RESTAURANT_DTO_COMPARATOR = Comparator.comparing(RestaurantDTO::getId)
            .thenComparing(RestaurantDTO::getName)
            .thenComparing(RestaurantDTO::getEmail)
            .thenComparing(RestaurantDTO::getPhoneNo)
            .thenComparing(RestaurantDTO::getCountry)
            .thenComparing(RestaurantDTO::getCity)
            .thenComparing(RestaurantDTO::getAddress)
            .thenComparing(RestaurantDTO::getDescription)
            .thenComparing(RestaurantDTO::getMaxCustomersNo)
            .thenComparing(RestaurantDTO::getMaxTablesNo);

    public static final Comparator<Booking> BOOKING_COMPARATOR = Comparator.comparing(Booking::getId)
            .thenComparing(Booking::getDateHour)
            .thenComparing(Booking::getCustomersNo)
            .thenComparing(Booking::getTablesNo);

    public static final Comparator<BookingDTO> BOOKING_DTO_COMPARATOR = Comparator.comparing(BookingDTO::getId)
            .thenComparing(BookingDTO::getCustomerId)
            .thenComparing(BookingDTO::getRestaurantId)
            .thenComparing(BookingDTO::getDateHour)
            .thenComparing(BookingDTO::getCustomersNo)
            .thenComparing(BookingDTO::getTablesNo);

    public static final Comparator<UserResponse> USER_RESPONSE_COMPARATOR = Comparator.comparing(UserResponse::getUsername)
            .thenComparing(UserResponse::getEmail)
            .thenComparing(UserResponse::getFullName)
            .thenComparing(UserResponse::getPhoneNo)
            .thenComparing(UserResponse::getCountry)
            .thenComparing(UserResponse::getCity);
}
