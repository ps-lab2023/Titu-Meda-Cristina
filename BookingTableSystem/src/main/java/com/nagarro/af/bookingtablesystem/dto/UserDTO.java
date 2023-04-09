package com.nagarro.af.bookingtablesystem.dto;

import com.nagarro.af.bookingtablesystem.annotation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
    @Id
    private UUID id;
    @NotBlank(message = "Username is mandatory!")
    private String username;
    @NotBlank(message = "Password is mandatory!")
    private String password;
    @NotBlank(message = "Name is mandatory!")
    private String fullName;
    @Email
    @UniqueEmail(message = "This email is already taken!")
    private String email;
    @NotBlank(message = "Phone number is mandatory!")
    @Pattern(regexp = "(([+]|[0][0])*[0-9]){10,15}", message = "Phone number is invalid!")
    private String phoneNo;
    private String country;
    private String city;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.country = country;
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
