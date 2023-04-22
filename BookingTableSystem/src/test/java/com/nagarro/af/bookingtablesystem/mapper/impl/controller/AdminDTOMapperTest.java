package com.nagarro.af.bookingtablesystem.mapper.impl.controller;

import com.nagarro.af.bookingtablesystem.controller.response.AdminResponse;
import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
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

@SpringBootTest(classes = AdminDTOMapper.class)
public class AdminDTOMapperTest {

    @Autowired
    private AdminDTOMapper adminDTOMapper;

    private final Comparator<UserResponse> adminResponseComparator = TestComparators.USER_RESPONSE_COMPARATOR;

    @Test
    public void whenGivenAdminDTO_thenReturnResponseWithSameFieldValues() {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();

        AdminResponse mappedAdminDTO = adminDTOMapper.mapDTOToResponse(adminDTO);

        assertNotNull(mappedAdminDTO);
        assertThat(adminResponseComparator.compare(adminResponse, mappedAdminDTO)).isZero();
    }

    @Test
    public void whenGivenAdminDTOWithNullValues_thenReturnResponseWithSameFieldValues() {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setCountry(null);
        adminDTO.setCity(null);
        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();
        adminResponse.setCountry(null);
        adminResponse.setCity(null);

        AdminResponse mappedAdminDTO = adminDTOMapper.mapDTOToResponse(adminDTO);

        assertNotNull(mappedAdminDTO);
        assertNull(mappedAdminDTO.getCountry());
        assertNull(mappedAdminDTO.getCity());
        assertEquals(adminResponse.getUsername(), mappedAdminDTO.getUsername());
        assertEquals(adminResponse.getEmail(), mappedAdminDTO.getEmail());
        assertEquals(adminResponse.getFullName(), mappedAdminDTO.getFullName());
        assertEquals(adminResponse.getPhoneNo(), mappedAdminDTO.getPhoneNo());
    }

    @Test
    public void whenGivenNullAdminDTO_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> adminDTOMapper.mapDTOToResponse(null));
    }

    @Test
    public void whenGivenAdminDTOList_thenReturnResponseListWithSameFieldValues() {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        List<AdminDTO> adminDTOList = new ArrayList<>();
        adminDTOList.add(adminDTO);

        AdminResponse adminResponse = TestDataBuilder.buildAdminResponse();
        List<AdminResponse> adminResponseList = new ArrayList<>();
        adminResponseList.add(adminResponse);

        List<AdminResponse> mappedAdminDTOList = adminDTOMapper.mapDTOListToResponseList(adminDTOList);

        assertNotNull(mappedAdminDTOList);
        assertTrue(adminResponseList.containsAll(mappedAdminDTOList));
    }

    @Test
    public void whenGivenAdminDTOEmptyList_thenReturnResponseWithSameFieldValues() {
        List<AdminResponse> mappedAdminDTOList = adminDTOMapper.mapDTOListToResponseList(new ArrayList<>());

        assertNotNull(mappedAdminDTOList);
        assertTrue(mappedAdminDTOList.isEmpty());
    }
}
