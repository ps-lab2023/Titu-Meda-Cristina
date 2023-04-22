package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    public Admin() {
    }

    public Admin(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }
}