package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "phone_no", nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String description;
    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Menu menu;
    @Column(name = "max_customers_no", nullable = false)
    private int maxCustomersNo;
    @Column(name = "max_tables_no", nullable = false)
    private int maxTablesNo;
    @ManyToOne(fetch = FetchType.LAZY)
    private RestaurantManager restaurantManager;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "restaurant_dates_capacity",
            joinColumns = {@JoinColumn(name = "restaurant_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_capacity_id", referencedColumnName = "id")})
    @MapKeyTemporal(TemporalType.DATE)
    private Map<Date, RestaurantCapacity> dateCapacityAvailability = new HashMap<>();

    public Restaurant() {
    }

    public Restaurant(String name, String email, String phoneNo, String country, String city, String address,
                      String description, Menu menu, int maxCustomersNo, int maxTablesNo, RestaurantManager restaurantManager) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.city = city;
        this.address = address;
        this.description = description;
        this.menu = menu;
        this.maxCustomersNo = maxCustomersNo;
        this.maxTablesNo = maxTablesNo;
        this.restaurantManager = restaurantManager;
    }

    public Restaurant(String name, String email, String phoneNo, String country, String city, String address,
                      String description, Menu menu, int maxCustomersNo, int maxTablesNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.city = city;
        this.address = address;
        this.description = description;
        this.menu = menu;
        this.maxCustomersNo = maxCustomersNo;
        this.maxTablesNo = maxTablesNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getMaxCustomersNo() {
        return maxCustomersNo;
    }

    public void setMaxCustomersNo(int maxCapacity) {
        this.maxCustomersNo = maxCapacity;
    }

    public int getMaxTablesNo() {
        return maxTablesNo;
    }

    public void setMaxTablesNo(int maxTablesNo) {
        this.maxTablesNo = maxTablesNo;
    }

    public Map<Date, RestaurantCapacity> getDateCapacityAvailability() {
        return dateCapacityAvailability;
    }

    public void setDateCapacityAvailability(Map<Date, RestaurantCapacity> dateCapacityAvailability) {
        this.dateCapacityAvailability = dateCapacityAvailability;
    }

    public RestaurantManager getRestaurantManager() {
        return restaurantManager;
    }

    public void setRestaurantManager(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", menu=" + menu +
                ", maxCustomersNo=" + maxCustomersNo +
                ", maxTablesNo=" + maxTablesNo +
                ", restaurantManager=" + restaurantManager +
                '}';
    }
}
