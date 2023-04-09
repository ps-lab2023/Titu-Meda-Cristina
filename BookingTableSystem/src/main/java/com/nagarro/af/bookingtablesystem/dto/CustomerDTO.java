package com.nagarro.af.bookingtablesystem.dto;

public class CustomerDTO extends UserDTO {

    public CustomerDTO(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }
}
