package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.CustomerResponse;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/customers")
public interface CustomerController extends UserController<CustomerDTO, CustomerResponse> {
}
