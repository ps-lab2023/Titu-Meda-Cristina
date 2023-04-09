package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.service.impl.RestaurantServiceImpl;
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
public class RestaurantServiceImplTest {

    private static final UUID RESTAURANT_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_ID);
    private static final Restaurant RESTAURANT_TEST = TestDataBuilder.buildRestaurant();
    private static final RestaurantDTO RESTAURANT_DTO_TEST = TestDataBuilder.buildRestaurantDTO();

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;
    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    @Captor
    private ArgumentCaptor<Restaurant> restaurantArgumentCaptor;

    @Test
    public void testSave_success() {
        RESTAURANT_TEST.setId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));

        when(restaurantMapper.mapDTOtoEntity(RESTAURANT_DTO_TEST)).thenReturn(RESTAURANT_TEST);
        when(restaurantRepository.save(RESTAURANT_TEST)).thenReturn(RESTAURANT_TEST);
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.save(RESTAURANT_DTO_TEST);

        verify(restaurantRepository).save(restaurantArgumentCaptor.capture());
        assertNotNull(returnedManagerDTO);
        assertThat(restaurantArgumentCaptor.getValue().getId()).isNotNull();
    }

    @Test
    public void testSave_returnNull() {
        when(restaurantMapper.mapDTOtoEntity(RESTAURANT_DTO_TEST)).thenReturn(RESTAURANT_TEST);
        when(restaurantRepository.save(RESTAURANT_TEST)).thenReturn(null);
        when(restaurantMapper.mapEntityToDTO(null)).thenReturn(null);

        RestaurantDTO returnedManagerDTO = restaurantService.save(RESTAURANT_DTO_TEST);

        assertNull(returnedManagerDTO);
    }

    @Test
    public void testFindById_success() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.findById(RESTAURANT_UUID_TEST);

        assertNotNull(returnedManagerDTO);
        assertNotNull(returnedManagerDTO.getId());
    }

    @Test
    public void testFindById_notFound() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantService.findById(RESTAURANT_UUID_TEST));
    }

    @Test
    public void testFindByName_success() {
        when(restaurantRepository.findByName(RESTAURANT_TEST.getName())).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.findByName(RESTAURANT_TEST.getName());

        assertNotNull(returnedManagerDTO);
        assertNotNull(returnedManagerDTO.getId());
    }

    @Test
    public void testFindByName_notFound() {
        when(restaurantRepository.findByName(RESTAURANT_TEST.getName())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantService.findByName(RESTAURANT_TEST.getName()));
    }

    @Test
    public void testFindAll_success() {
        List<Restaurant> managers = new ArrayList<>();
        managers.add(RESTAURANT_TEST);
        List<RestaurantDTO> managerDTOS = new ArrayList<>();
        managerDTOS.add(RESTAURANT_DTO_TEST);

        when(restaurantRepository.findAll()).thenReturn(managers);
        when(restaurantMapper.mapEntityListToDTOList(managers)).thenReturn(managerDTOS);

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAll();

        assertNotNull(returnedManagerDTOList);
        assertTrue(returnedManagerDTOList.containsAll(managerDTOS));
    }

    @Test
    public void testFindAll_returnEmptyList() {
        List<Restaurant> managers = new ArrayList<>();

        when(restaurantRepository.findAll()).thenReturn(managers);

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAll();
        assertTrue(returnedManagerDTOList.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_success() {
        Restaurant anotherRestaurant = TestDataBuilder.buildRestaurant();
        anotherRestaurant.setId(UUID.randomUUID());
        anotherRestaurant.setCity("Cluj-Napoca");

        RestaurantDTO anotherRestaurantDTO = TestDataBuilder.buildRestaurantDTO();
        anotherRestaurantDTO.setId(anotherRestaurant.getId());
        anotherRestaurantDTO.setCity("Cluj-Napoca");

        List<Restaurant> managers = new ArrayList<>();
        managers.add(RESTAURANT_TEST);
        managers.add(anotherRestaurant);

        List<RestaurantDTO> managerDTOS = new ArrayList<>();
        managerDTOS.add(RESTAURANT_DTO_TEST);
        managerDTOS.add(anotherRestaurantDTO);

        when(restaurantRepository.findAllByCountryAndCity("Romania", "Brasov")).thenReturn(managers);
        when(restaurantMapper.mapEntityListToDTOList(managers)).thenReturn(managerDTOS);

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAllByCountryAndCity("Romania", "Brasov");

        assertNotNull(returnedManagerDTOList);
        assertTrue(returnedManagerDTOList.containsAll(managerDTOS));
    }

    @Test
    public void testFindAllByCountryAndCity_returnEmptyList() {
        List<Restaurant> managers = new ArrayList<>();

        when(restaurantRepository.findAllByCountryAndCity("Austria", "Vienna")).thenReturn(managers);

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAllByCountryAndCity("Austria", "Vienna");
        assertTrue(returnedManagerDTOList.isEmpty());
    }

}
