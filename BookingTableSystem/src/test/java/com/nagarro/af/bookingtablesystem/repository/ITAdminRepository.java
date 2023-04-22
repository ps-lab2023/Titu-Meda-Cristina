package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ITAdminRepository extends ITBaseRepository {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void testFindByEmail_success() {
        Admin admin = adminRepository.saveAndFlush(TestDataBuilder.buildAdmin());

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(admin.getEmail());
        assertTrue(optionalAdmin.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail("nothing");
        assertTrue(optionalAdmin.isEmpty());
    }
}
