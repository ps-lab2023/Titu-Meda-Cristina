package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository AdminRepository, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.adminRepository = AdminRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminDTO save(AdminDTO adminDTO) {
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return modelMapper.map(adminRepository.save(admin), AdminDTO.class);
    }

    @Override
    public AdminDTO findById(UUID id) {
        return adminRepository.findById(id)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with id " + id +
                        " could not be found!"));
    }

    @Override
    public AdminDTO findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with email " + email +
                        " could not be found!"));
    }

    @Override
    public AdminDTO findByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with username " + username +
                        " could not be found!"));
    }

    @Override
    public List<AdminDTO> findAll() {
        List<Admin> admins = adminRepository.findAll();
        return listMapper.mapList(admins, AdminDTO.class);
    }

    @Override
    public void delete(UUID id) {
        adminRepository.deleteById(id);
    }

    private AdminDTO mapToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }
}
