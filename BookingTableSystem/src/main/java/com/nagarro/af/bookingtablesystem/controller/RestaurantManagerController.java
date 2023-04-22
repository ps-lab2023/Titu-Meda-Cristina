package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.RestaurantManagerResponse;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/managers")
public interface RestaurantManagerController extends UserController<RestaurantManagerDTO, RestaurantManagerResponse> {
}
