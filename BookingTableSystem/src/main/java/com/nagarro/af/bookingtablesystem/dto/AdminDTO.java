package com.nagarro.af.bookingtablesystem.dto;

public class AdminDTO extends UserDTO {

    public AdminDTO() {
    }

    public AdminDTO(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }
}
