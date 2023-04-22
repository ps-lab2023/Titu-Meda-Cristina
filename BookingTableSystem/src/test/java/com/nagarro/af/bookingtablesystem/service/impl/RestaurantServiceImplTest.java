package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    private static final UUID RESTAURANT_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_ID);

    private static final UUID MANAGER_UUID_TEST = UUID.fromString(TestDataBuilder.RESTAURANT_MANAGER_ID);

    private static final Restaurant RESTAURANT_TEST = TestDataBuilder.buildRestaurant();

    private static final RestaurantDTO RESTAURANT_DTO_TEST = TestDataBuilder.buildRestaurantDTO();

    private static final RestaurantManager MANAGER_TEST = TestDataBuilder.buildRestaurantManager();

    private static final Comparator<RestaurantDTO> RESTAURANT_DTO_COMPARATOR = TestComparators.RESTAURANT_DTO_COMPARATOR;

    private static final Comparator<MenuDTO> MENU_DTO_COMPARATOR = TestComparators.MENU_DTO_COMPARATOR;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantManagerRepository restaurantManagerRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    public void testSave_success() {
        RESTAURANT_TEST.setId(RESTAURANT_UUID_TEST);
        RESTAURANT_DTO_TEST.setId(RESTAURANT_UUID_TEST);

        when(restaurantMapper.mapDTOtoEntity(RESTAURANT_DTO_TEST)).thenReturn(RESTAURANT_TEST);
        when(restaurantRepository.save(RESTAURANT_TEST)).thenReturn(RESTAURANT_TEST);
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.save(RESTAURANT_DTO_TEST, null);

        assertNotNull(returnedManagerDTO);
        assertThat(RESTAURANT_DTO_COMPARATOR.compare(returnedManagerDTO, RESTAURANT_DTO_TEST)).isZero();
    }

    @Test
    public void testSaveWithMenu_success() {
        Restaurant restaurantWithMenu = buildRestaurantWithMenu();
        RestaurantDTO restaurantDTO = TestDataBuilder.buildRestaurantDTO();
        restaurantDTO.setId(RESTAURANT_UUID_TEST);


        MockMultipartFile menu = new MockMultipartFile(
                "menu", "menu.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "menu".getBytes()
        );

        when(restaurantMapper.mapDTOtoEntity(restaurantDTO)).thenReturn(restaurantWithMenu);
        when(restaurantRepository.save(restaurantWithMenu)).thenReturn(restaurantWithMenu);
        when(restaurantMapper.mapEntityToDTO(restaurantWithMenu)).thenReturn(restaurantDTO);

        RestaurantDTO returnedManagerDTO = restaurantService.save(restaurantDTO, menu);
        MenuDTO returnedMenuDTO = returnedManagerDTO.getMenuDTO();
        returnedMenuDTO.setId(RESTAURANT_UUID_TEST);

        assertNotNull(returnedManagerDTO);
        assertNotNull(returnedManagerDTO.getMenuDTO());
        assertThat(RESTAURANT_DTO_COMPARATOR.compare(returnedManagerDTO, restaurantDTO)).isZero();
        assertThat(MENU_DTO_COMPARATOR.compare(returnedMenuDTO, restaurantDTO.getMenuDTO())).isZero();
    }

    @Test
    public void testFindById_success() {
        RESTAURANT_TEST.setId(RESTAURANT_UUID_TEST);
        RESTAURANT_DTO_TEST.setId(RESTAURANT_UUID_TEST);

        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.findById(RESTAURANT_UUID_TEST);

        assertNotNull(returnedManagerDTO);
        assertThat(RESTAURANT_DTO_COMPARATOR.compare(returnedManagerDTO, RESTAURANT_DTO_TEST)).isZero();
    }

    @Test
    public void testFindById_notFound() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> restaurantService.findById(RESTAURANT_UUID_TEST));

        assertEquals(exception.getMessage(), "Restaurant with id " + RESTAURANT_UUID_TEST + " could not be found!");
    }

    @Test
    public void testFindByName_success() {
        RESTAURANT_TEST.setId(RESTAURANT_UUID_TEST);
        RESTAURANT_DTO_TEST.setId(RESTAURANT_UUID_TEST);

        when(restaurantRepository.findByName(RESTAURANT_TEST.getName())).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantMapper.mapEntityToDTO(RESTAURANT_TEST)).thenReturn(RESTAURANT_DTO_TEST);

        RestaurantDTO returnedManagerDTO = restaurantService.findByName(RESTAURANT_TEST.getName());

        assertNotNull(returnedManagerDTO);
        assertThat(RESTAURANT_DTO_COMPARATOR.compare(returnedManagerDTO, RESTAURANT_DTO_TEST)).isZero();
    }

    @Test
    public void testFindByName_notFound() {
        when(restaurantRepository.findByName(RESTAURANT_TEST.getName())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                restaurantService.findByName(RESTAURANT_TEST.getName()));

        assertEquals(exception.getMessage(), "Restaurant with name " + RESTAURANT_TEST.getName() + " could not be found!");
    }

    @Test
    public void testFindAll_success() {
        RESTAURANT_TEST.setId(RESTAURANT_UUID_TEST);
        RESTAURANT_DTO_TEST.setId(RESTAURANT_UUID_TEST);

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
        when(restaurantRepository.findAll()).thenReturn(new ArrayList<>());

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAll();

        assertTrue(returnedManagerDTOList.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_success() {
        RESTAURANT_TEST.setId(RESTAURANT_UUID_TEST);
        RESTAURANT_DTO_TEST.setId(RESTAURANT_UUID_TEST);

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
        when(restaurantRepository.findAllByCountryAndCity("Austria", "Vienna")).thenReturn(new ArrayList<>());

        List<RestaurantDTO> returnedManagerDTOList = restaurantService.findAllByCountryAndCity("Austria", "Vienna");

        assertTrue(returnedManagerDTOList.isEmpty());
    }

    @Test
    public void testDelete_success() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.of(RESTAURANT_TEST));

        assertDoesNotThrow(() -> restaurantService.delete(RESTAURANT_UUID_TEST));
    }

    @Test
    public void testDelete_restaurantNotFound() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                restaurantService.delete(RESTAURANT_UUID_TEST));

        assertEquals(exception.getMessage(), "Restaurant with id " + RESTAURANT_UUID_TEST + " could not be found!");
    }

    @Test
    public void testAssignRestaurantManager_success() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.of(MANAGER_TEST));

        assertDoesNotThrow(() -> restaurantService.assignRestaurantManager(RESTAURANT_UUID_TEST, MANAGER_UUID_TEST));
    }

    @Test
    public void testAssignRestaurantManager_restaurantNotFound() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                restaurantService.assignRestaurantManager(RESTAURANT_UUID_TEST, MANAGER_UUID_TEST));

        assertEquals(exception.getMessage(), "Restaurant with id " + RESTAURANT_UUID_TEST + " could not be found!");
    }

    @Test
    public void testAssignRestaurantManager_managerNotFound() {
        when(restaurantRepository.findById(RESTAURANT_UUID_TEST)).thenReturn(Optional.of(RESTAURANT_TEST));
        when(restaurantManagerRepository.findById(MANAGER_UUID_TEST)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                restaurantService.assignRestaurantManager(RESTAURANT_UUID_TEST, MANAGER_UUID_TEST));

        assertEquals(exception.getMessage(), "Restaurant manager with id " + MANAGER_UUID_TEST + " could not be found!");
    }

    private Restaurant buildRestaurantWithMenu() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        restaurant.setId(RESTAURANT_UUID_TEST);

        Menu menu = TestDataBuilder.buildMenu(restaurant);
        assert menu != null;
        menu.setId(RESTAURANT_UUID_TEST);

        restaurant.setMenu(menu);
        return restaurant;
    }
}
