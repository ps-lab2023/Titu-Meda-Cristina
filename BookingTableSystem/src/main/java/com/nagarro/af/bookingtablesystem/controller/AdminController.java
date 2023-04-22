package com.nagarro.af.bookingtablesystem.controller;

import com.nagarro.af.bookingtablesystem.controller.response.AdminResponse;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/admins")
public interface AdminController extends UserController<AdminDTO, AdminResponse> {
}
