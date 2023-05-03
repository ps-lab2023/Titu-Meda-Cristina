package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.User;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    private static final UUID ADMIN_UUID_TEST = UUID.fromString(TestDataBuilder.ADMIN_ID);

    private static final String ENCRYPTED_PASSWORD = "$2a$10$YzGNtx2VeQUW.ml7JEGWP.u6s5kXhCgxTB4t/Jykc8U.6LRkn7ORS";

    private static final Admin ADMIN_TEST = TestDataBuilder.buildAdmin();

    private static final AdminDTO ADMIN_DTO_TEST = TestDataBuilder.buildAdminDTO();

    private static final Comparator<User> USER_COMPARATOR = TestComparators.USER_COMPARATOR;

    private static final Comparator<UserDTO> USER_DTO_COMPARATOR = TestComparators.USER_DTO_COMPARATOR;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ListMapper listMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Captor
    private ArgumentCaptor<Admin> adminArgumentCaptor;

    @Test
    public void testSave_success() {
        ADMIN_TEST.setId(ADMIN_UUID_TEST);
        ADMIN_DTO_TEST.setId(ADMIN_UUID_TEST);

        Admin adminEncryptedPassword = getAdminEncryptedPassword();

        AdminDTO adminDTOEncryptedPassword = getAdminDTOEncryptedPassword();

        when(modelMapper.map(ADMIN_DTO_TEST, Admin.class)).thenReturn(ADMIN_TEST);
        when(passwordEncoder.encode(ADMIN_TEST.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(adminRepository.save(adminArgumentCaptor.capture())).thenReturn(adminEncryptedPassword);
        when(modelMapper.map(adminEncryptedPassword, AdminDTO.class)).thenReturn(adminDTOEncryptedPassword);

        AdminDTO returnedAdminDTO = adminService.save(ADMIN_DTO_TEST);

        assertNotNull(returnedAdminDTO);
        assertThat(USER_COMPARATOR.compare(adminArgumentCaptor.getValue(), adminEncryptedPassword)).isZero();
        assertThat(USER_DTO_COMPARATOR.compare(returnedAdminDTO, adminDTOEncryptedPassword)).isZero();
    }

    @Test
    public void testSave_returnNull() {
        when(modelMapper.map(ADMIN_DTO_TEST, Admin.class)).thenReturn(ADMIN_TEST);
        when(adminRepository.save(ADMIN_TEST)).thenReturn(null);
        when(modelMapper.map(null, AdminDTO.class)).thenReturn(null);

        AdminDTO returnedAdminDTO = adminService.save(ADMIN_DTO_TEST);

        assertNull(returnedAdminDTO);
    }

    @Test
    public void testFindById_success() {
        ADMIN_TEST.setId(ADMIN_UUID_TEST);
        ADMIN_DTO_TEST.setId(ADMIN_UUID_TEST);

        when(adminRepository.findById(ADMIN_UUID_TEST)).thenReturn(Optional.of(ADMIN_TEST));
        when(modelMapper.map(ADMIN_TEST, AdminDTO.class)).thenReturn(ADMIN_DTO_TEST);

        AdminDTO returnedAdminDTO = adminService.findById(ADMIN_UUID_TEST);

        assertNotNull(returnedAdminDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedAdminDTO, ADMIN_DTO_TEST)).isZero();
    }

    @Test
    public void testFindById_notFound() {
        when(adminRepository.findById(ADMIN_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adminService.findById(ADMIN_UUID_TEST));
    }

    @Test
    public void testFindByEmail_success() {
        ADMIN_TEST.setId(ADMIN_UUID_TEST);
        ADMIN_DTO_TEST.setId(ADMIN_UUID_TEST);

        when(adminRepository.findByEmail(ADMIN_TEST.getEmail())).thenReturn(Optional.of(ADMIN_TEST));
        when(modelMapper.map(ADMIN_TEST, AdminDTO.class)).thenReturn(ADMIN_DTO_TEST);

        AdminDTO returnedAdminDTO = adminService.findByEmail(ADMIN_TEST.getEmail());

        assertNotNull(returnedAdminDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedAdminDTO, ADMIN_DTO_TEST)).isZero();
    }

    @Test
    public void testFindByEmail_notFound() {
        when(adminRepository.findByEmail(ADMIN_TEST.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adminService.findByEmail(ADMIN_TEST.getEmail()));
    }

    @Test
    public void testFindAll_success() {
        List<Admin> Admins = new ArrayList<>();
        Admins.add(ADMIN_TEST);
        List<AdminDTO> AdminDTOS = new ArrayList<>();
        AdminDTOS.add(ADMIN_DTO_TEST);

        when(adminRepository.findAll()).thenReturn(Admins);
        when(listMapper.mapList(Admins, AdminDTO.class)).thenReturn(AdminDTOS);

        List<AdminDTO> returnedAdminDTOList = adminService.findAll();

        assertNotNull(returnedAdminDTOList);
        assertTrue(returnedAdminDTOList.containsAll(AdminDTOS));
    }

    @Test
    public void testFindAll_returnEmptyList() {
        List<Admin> Admins = new ArrayList<>();

        when(adminRepository.findAll()).thenReturn(Admins);

        List<AdminDTO> returnedAdminDTOList = adminService.findAll();

        assertTrue(returnedAdminDTOList.isEmpty());
    }

    @NotNull
    private Admin getAdminEncryptedPassword() {
        Admin adminEncryptedPassword = TestDataBuilder.buildAdmin();
        adminEncryptedPassword.setId(ADMIN_UUID_TEST);
        adminEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);
        return adminEncryptedPassword;
    }

    @NotNull
    private AdminDTO getAdminDTOEncryptedPassword() {
        AdminDTO adminDTOEncryptedPassword = TestDataBuilder.buildAdminDTO();
        adminDTOEncryptedPassword.setId(ADMIN_UUID_TEST);
        adminDTOEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);
        return adminDTOEncryptedPassword;
    }
}
