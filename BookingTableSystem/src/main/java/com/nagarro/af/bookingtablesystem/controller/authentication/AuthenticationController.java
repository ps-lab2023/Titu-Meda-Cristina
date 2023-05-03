package com.nagarro.af.bookingtablesystem.controller.authentication;

import com.nagarro.af.bookingtablesystem.controller.authentication.request.AuthenticationRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.request.RegisterRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/auth")
public interface AuthenticationController {

    @PostMapping(path = "/register")
    ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request);

    @PostMapping(path = "/authenticate")
    ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request);
}
