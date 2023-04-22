package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.RestaurantManagerResponse;
import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RestaurantManagerDTOMapper.class)
public class RestaurantManagerDTOMapperTest {

    @Autowired
    private RestaurantManagerDTOMapper restaurantManagerDTOMapper;

    private final Comparator<UserResponse> managerResponseComparator = TestComparators.USER_RESPONSE_COMPARATOR;

    @Test
    public void whenGivenRestaurantManagerDTO_thenReturnResponseWithSameFieldValues() {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();

        RestaurantManagerResponse mappedManagerDTO = restaurantManagerDTOMapper.mapDTOToResponse(managerDTO);

        assertNotNull(mappedManagerDTO);
        assertThat(managerResponseComparator.compare(managerResponse, mappedManagerDTO)).isZero();
    }

    @Test
    public void whenGivenRestaurantManagerDTOWithNullValues_thenReturnResponseWithSameFieldValues() {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        managerDTO.setCountry(null);
        managerDTO.setCity(null);
        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();
        managerResponse.setCountry(null);
        managerResponse.setCity(null);

        RestaurantManagerResponse mappedManagerDTO = restaurantManagerDTOMapper.mapDTOToResponse(managerDTO);

        assertNotNull(mappedManagerDTO);
        assertNull(mappedManagerDTO.getCountry());
        assertNull(mappedManagerDTO.getCity());
        assertEquals(managerResponse.getUsername(), mappedManagerDTO.getUsername());
        assertEquals(managerResponse.getEmail(), mappedManagerDTO.getEmail());
        assertEquals(managerResponse.getFullName(), mappedManagerDTO.getFullName());
        assertEquals(managerResponse.getPhoneNo(), mappedManagerDTO.getPhoneNo());
    }

    @Test
    public void whenGivenNullRestaurantManagerDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantManagerDTOMapper.mapDTOToResponse(null));
    }

    @Test
    public void whenGivenRestaurantManagerDTOList_thenReturnResponseListWithSameFieldValues() {
        RestaurantManagerDTO managerDTO = TestDataBuilder.buildRestaurantManagerDTO();
        List<RestaurantManagerDTO> managerDTOList = new ArrayList<>();
        managerDTOList.add(managerDTO);

        RestaurantManagerResponse managerResponse = TestDataBuilder.buildRestaurantManagerResponse();
        List<RestaurantManagerResponse> managerResponseList = new ArrayList<>();
        managerResponseList.add(managerResponse);

        List<RestaurantManagerResponse> mappedManagerDTOList = restaurantManagerDTOMapper.mapDTOListToResponseList(managerDTOList);

        assertNotNull(mappedManagerDTOList);
        assertTrue(managerResponseList.containsAll(mappedManagerDTOList));
    }

    @Test
    public void whenGivenRestaurantManagerDTOEmptyList_thenReturnResponseWithSameFieldValues() {
        List<RestaurantManagerResponse> mappedManagerDTOList = restaurantManagerDTOMapper.mapDTOListToResponseList(new ArrayList<>());

        assertNotNull(mappedManagerDTOList);
        assertTrue(mappedManagerDTOList.isEmpty());
    }
}
