package com.nagarro.af.bookingtablesystem.controller.impl;

import com.nagarro.af.bookingtablesystem.controller.AdminController;
import com.nagarro.af.bookingtablesystem.controller.response.AdminResponse;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.mapper.impl.controller.AdminDTOMapper;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    private final AdminDTOMapper adminDTOMapper;

    public AdminControllerImpl(AdminService adminService, AdminDTOMapper adminDTOMapper) {
        this.adminService = adminService;
        this.adminDTOMapper = adminDTOMapper;
    }

    @Override
    public ResponseEntity<AdminResponse> save(AdminDTO adminDTO) {
        return new ResponseEntity<>(mapToAdminResponse(adminService.save(adminDTO)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AdminResponse> findById(UUID id) {
        return new ResponseEntity<>(mapToAdminResponse(adminService.findById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AdminResponse>> findAll() {
        return new ResponseEntity<>(mapToAdminResponseList(adminService.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        adminService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private AdminResponse mapToAdminResponse(AdminDTO adminDTO) {
        return adminDTOMapper.mapDTOToResponse(adminDTO);
    }

    private List<AdminResponse> mapToAdminResponseList(List<AdminDTO> adminDTOList) {
        return adminDTOMapper.mapDTOListToResponseList(adminDTOList);
    }
}
