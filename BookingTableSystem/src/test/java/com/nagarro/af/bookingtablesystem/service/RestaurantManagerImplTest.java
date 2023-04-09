package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.RestaurantManagerMapper;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.impl.RestaurantManagerServiceImpl;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantManagerImplTest {

    private static final UUID MANAGER_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID);
    private static final RestaurantManager MANAGER_TEST = TestDataBuilder.buildRestaurantManager();
    private static final RestaurantManagerDTO MANAGER_DTO_TEST = TestDataBuilder.buildRestaurantManagerDTO();

    @Mock
    private RestaurantManagerRepository restaurantManagerRepository;
    @Mock
    private RestaurantManagerMapper managerMapper;
    @InjectMocks
    private RestaurantManagerServiceImpl restaurantManagerService;
    @Captor
    private ArgumentCaptor<RestaurantManager> managerArgumentCaptor;

    @Test
    public void testSave_success() {
        when(managerMapper.mapDTOtoEntity(MANAGER_DTO_TEST)).thenReturn(MANAGER_TEST);
        when(restaurantManagerRepository.save(MANAGER_TEST)).thenReturn(MANAGER_TEST);
        when(managerMapper.mapEntityToDTO(MANAGER_TEST)).thenReturn(MANAGER_DTO_TEST);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.save(MANAGER_DTO_TEST);

        verify(restaurantManagerRepository).save(managerArgumentCaptor.capture());
        assertNotNull(returnedManagerDTO);
        assertThat(managerArgumentCaptor.getValue().getId()).isNotNull();
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
        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.of(MANAGER_TEST));
        when(managerMapper.mapEntityToDTO(MANAGER_TEST)).thenReturn(MANAGER_DTO_TEST);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.findById(MANAGER_UUID_TEST);

        assertNotNull(returnedManagerDTO);
        assertNotNull(returnedManagerDTO.getId());
    }

    @Test
    public void testFindById_notFound() {
        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantManagerService.findById(MANAGER_UUID_TEST));
    }

    @Test
    public void testFindByEmail_success() {
        when(restaurantManagerRepository.findByEmail(MANAGER_TEST.getEmail())).thenReturn(Optional.of(MANAGER_TEST));
        when(managerMapper.mapEntityToDTO(MANAGER_TEST)).thenReturn(MANAGER_DTO_TEST);

        RestaurantManagerDTO returnedManagerDTO = restaurantManagerService.findByEmail(MANAGER_TEST.getEmail());

        assertNotNull(returnedManagerDTO);
        assertNotNull(returnedManagerDTO.getId());
    }

    @Test
    public void testFindByEmail_notFound() {
        when(restaurantManagerRepository.findByEmail(MANAGER_TEST.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantManagerService.findByEmail(MANAGER_TEST.getEmail()));
    }

    @Test
    public void testFindAll_success() {
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
