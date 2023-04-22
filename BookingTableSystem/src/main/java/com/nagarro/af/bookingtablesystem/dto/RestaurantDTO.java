package com.nagarro.af.bookingtablesystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.UUID;

public class RestaurantDTO {
    @Id
    private UUID id;
    @NotBlank(message = "Restaurant's name is mandatory!")
    private String name;
    @NotBlank(message = "Restaurant's email is mandatory!")
    private String email;
    @NotBlank(message = "Restaurant's phone number is mandatory!")
    private String phoneNo;
    @NotBlank(message = "Country is mandatory!")
    private String country;
    @NotBlank(message = "City is mandatory!")
    private String city;
    @NotBlank(message = "Restaurant's address is mandatory!")
    private String address;
    @NotBlank(message = "Restaurant's description is mandatory!")
    private String description;
    @JsonProperty("menu")
    private MenuDTO menuDTO;
    @NotNull(message = "Maximum number of customers per day must be set!")
    private Integer maxCustomersNo;
    @NotNull(message = "Maximum number of tables per day must be set!")
    private Integer maxTablesNo;
    @JsonProperty("managerId")
    private UUID restaurantManagerId;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String name, String email, String phoneNo, String country, String city, String address,
                         String description, Integer maxCustomersNo, Integer maxTablesNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.city = city;
        this.address = address;
        this.description = description;
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

    public Integer getMaxCustomersNo() {
        return maxCustomersNo;
    }

    public void setMaxCustomersNo(Integer maxCustomersNo) {
        this.maxCustomersNo = maxCustomersNo;
    }

    public Integer getMaxTablesNo() {
        return maxTablesNo;
    }

    public void setMaxTablesNo(Integer maxTablesNo) {
        this.maxTablesNo = maxTablesNo;
    }

    public MenuDTO getMenuDTO() {
        return menuDTO;
    }

    public void setMenuDTO(MenuDTO menuDTO) {
        this.menuDTO = menuDTO;
    }

    public UUID getRestaurantManagerId() {
        return restaurantManagerId;
    }

    public void setRestaurantManagerId(UUID restaurantManagerId) {
        this.restaurantManagerId = restaurantManagerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDTO that = (RestaurantDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
