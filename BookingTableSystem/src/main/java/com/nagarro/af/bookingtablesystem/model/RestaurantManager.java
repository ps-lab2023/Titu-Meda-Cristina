package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "restaurant_managers")
public class RestaurantManager extends User implements UserDetails {
    private static final String ROLE = "ROLE_RESTAURANT_MANAGER";

    @OneToMany(mappedBy = "restaurantManager")
    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantManager() {
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city, Restaurant restaurant) {
        super(username, password, fullName, email, phoneNo, country, city);
        this.restaurants.add(restaurant);
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city, List<Restaurant> restaurants) {
        super(username, password, fullName, email, phoneNo, country, city);
        this.restaurants.addAll(restaurants);
    }

    public RestaurantManager(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        for (Restaurant restaurant : restaurants) {
            restaurant.setRestaurantManager(this);
        }
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        restaurant.setRestaurantManager(this);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
        restaurant.setRestaurantManager(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
