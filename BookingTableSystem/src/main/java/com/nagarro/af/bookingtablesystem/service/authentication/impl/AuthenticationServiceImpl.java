package com.nagarro.af.bookingtablesystem.service.authentication.impl;

import com.nagarro.af.bookingtablesystem.controller.authentication.request.AuthenticationRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.response.AuthenticationResponse;
import com.nagarro.af.bookingtablesystem.controller.authentication.request.RegisterRequest;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.security.JwtService;
import com.nagarro.af.bookingtablesystem.service.authentication.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerRepository restaurantManagerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(AdminRepository adminRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantManagerRepository restaurantManagerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        String jwtToken;
        switch (request.getRole()) {
            case "ADMIN" -> {
                Admin admin = new Admin(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                adminRepository.save(admin);
                jwtToken = jwtService.generateToken(admin);
            }
            case "CUSTOMER" -> {
                Customer customer = new Customer(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                customerRepository.save(customer);
                jwtToken = jwtService.generateToken(customer);
            }
            case "RESTAURANT_MANAGER" -> {
                RestaurantManager manager = new RestaurantManager(
                        request.getUsername(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getFullName(),
                        request.getEmail(),
                        request.getPhoneNo(),
                        request.getCountry(),
                        request.getCity()
                );
                restaurantManagerRepository.save(manager);
                jwtToken = jwtService.generateToken(manager);
            }
            default -> throw new IllegalStateException("Unexpected role value: " + request.getRole());
        }
        return new AuthenticationResponse(jwtToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(request.getUsername());
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(request.getUsername());
        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByUsername(
                request.getUsername()
        );

        String jwtToken = "";

        if (optionalAdmin.isPresent()) {
            jwtToken = jwtService.generateToken(optionalAdmin.get());
        }

        if (optionalCustomer.isPresent()) {
            jwtToken = jwtService.generateToken(optionalCustomer.get());
        }

        if (optionalRestaurantManager.isPresent()) {
            jwtToken = jwtService.generateToken(optionalRestaurantManager.get());
        }

        if (jwtToken.isEmpty()) {
            throw new NotFoundException("User with username " + request.getUsername() + " could not be found!");
        }

        return new AuthenticationResponse(jwtToken);
    }
}
