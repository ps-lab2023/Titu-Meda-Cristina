package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantManagerMapper;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.model.User;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantManagerImplTest {

    private static final UUID MANAGER_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID);

    private static final String ENCRYPTED_PASSWORD = "$2a$10$YzGNtx2VeQUW.ml7JEGWP.u6s5kXhCgxTB4t/Jykc8U.6LRkn7ORS";

    private static final RestaurantManager MANAGER_TEST = TestDataBuilder.buildRestaurantManager();

    private static final RestaurantManagerDTO MANAGER_DTO_TEST = TestDataBuilder.buildRestaurantManagerDTO();

    private static final Comparator<User> USER_COMPARATOR = TestComparators.USER_COMPARATOR;

    private static final Comparator<UserDTO> USER_DTO_COMPARATOR = TestComparators.USER_DTO_COMPARATOR;

    @Mock
    private RestaurantManagerRepository restaurantManagerRepository;

    @Mock
    private RestaurantManagerMapper managerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RestaurantManagerServiceImpl restaurantManagerService;

    @Captor
    private ArgumentCaptor<RestaurantManager> managerArgumentCaptor;

    @Test
    public void testSave_success() {
        MANAGER_TEST.setId(MANAGER_UUID_TEST);
        MANAGER_DTO_TEST.setId(MANAGER_UUID_TEST);

        RestaurantManager managerEncryptedPassword = TestDataBuilder.buildRestaurantManager();
        managerEncryptedPassword.setId(MANAGER_UUID_TEST);
        managerEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);

        RestaurantManagerDTO managerDTOEncryptedPassword = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTOEncryptedPassword.setId(MANAGER_UUID_TEST);
        managerDTOEncryptedPassword.setPassword(ENCRYPTED_PASSWORD);

        when(managerMapper.mapDTOtoEntity(MANAGER_DTO_TEST)).thenReturn(MANAGER_TEST);
        when(passwordEncoder.encode(MANAGER_TEST.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(restaurantManagerRepository.save(managerArgumentCaptor.capture())).thenReturn(managerEncryptedPassword);
        when(managerMapper.mapEntityToDTO(managerEncryptedPassword)).thenReturn(managerDTOEncryptedPassword);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.save(MANAGER_DTO_TEST);

        assertNotNull(returnedManagerDTO);
        assertThat(USER_COMPARATOR.compare(managerEncryptedPassword, managerArgumentCaptor.getValue())).isZero();
        assertThat(USER_DTO_COMPARATOR.compare(returnedManagerDTO, managerDTOEncryptedPassword)).isZero();
    }

    @Test
    public void testSave_returnNull() {
        when(managerMapper.mapDTOtoEntity(MANAGER_DTO_TEST)).thenReturn(MANAGER_TEST);
        when(restaurantManagerRepository.save(MANAGER_TEST)).thenReturn(null);
        when(managerMapper.mapEntityToDTO(null)).thenReturn(null);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.save(MANAGER_DTO_TEST);

        assertNull(returnedManagerDTO);
    }

    @Test
    public void testFindById_success() {
        MANAGER_TEST.setId(MANAGER_UUID_TEST);
        MANAGER_DTO_TEST.setId(MANAGER_UUID_TEST);

        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.of(MANAGER_TEST));
        when(managerMapper.mapEntityToDTO(MANAGER_TEST)).thenReturn(MANAGER_DTO_TEST);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.findById(MANAGER_UUID_TEST);

        assertNotNull(returnedManagerDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedManagerDTO, MANAGER_DTO_TEST)).isZero();
    }

    @Test
    public void testFindById_notFound() {
        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantManagerService.findById(MANAGER_UUID_TEST));
    }

    @Test
    public void testFindByEmail_success() {
        MANAGER_TEST.setId(MANAGER_UUID_TEST);
        MANAGER_DTO_TEST.setId(MANAGER_UUID_TEST);

        when(restaurantManagerRepository.findByEmail(MANAGER_TEST.getEmail())).thenReturn(Optional.of(MANAGER_TEST));
        when(managerMapper.mapEntityToDTO(MANAGER_TEST)).thenReturn(MANAGER_DTO_TEST);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.findByEmail(MANAGER_TEST.getEmail());

        assertNotNull(returnedManagerDTO);
        assertThat(USER_DTO_COMPARATOR.compare(returnedManagerDTO, MANAGER_DTO_TEST)).isZero();
    }

    @Test
    public void testFindByEmail_notFound() {
        when(restaurantManagerRepository.findByEmail(MANAGER_TEST.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantManagerService.findByEmail(MANAGER_TEST.getEmail()));
    }

    @Test
    public void testFindAll_success() {
        MANAGER_TEST.setId(MANAGER_UUID_TEST);
        MANAGER_DTO_TEST.setId(MANAGER_UUID_TEST);

        List<RestaurantManager> managers = new ArrayList<>();
        managers.add(MANAGER_TEST);
        List<RestaurantManagerDTO> managerDTOS = new ArrayList<>();
        managerDTOS.add(MANAGER_DTO_TEST);

        when(restaurantManagerRepository.findAll()).thenReturn(managers);
        when(managerMapper.mapEntityListToDTOList(managers)).thenReturn(managerDTOS);

        List<RestaurantManagerDTO> returnedManagerDTOList = restaurantManagerService.findAll();

        assertNotNull(returnedManagerDTOList);
        assertTrue(returnedManagerDTOList.containsAll(managerDTOS));
    }

    @Test
    public void testFindAll_returnEmptyList() {
        List<RestaurantManager> managers = new ArrayList<>();

        when(restaurantManagerRepository.findAll()).thenReturn(managers);

        List<RestaurantManagerDTO> returnedManagerDTOList = restaurantManagerService.findAll();
        assertTrue(returnedManagerDTOList.isEmpty());
    }
}
