package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Customer() {

    }

    public Customer(String username, String password, String fullName, String email, String phoneNo, String country, String city) {
        super(username, password, fullName, email, phoneNo, country, city);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
