package com.nagarro.af.bookingtablesystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.handler.ApiException;
import com.nagarro.af.bookingtablesystem.service.BookingService;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingControllerImpl.class)
public class ITBookingControllerImpl {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final UUID CUSTOMER_ID = UUID.fromString(TestDataBuilder.CUSTOMER_ID);

    private static final UUID RESTAURANT_ID = UUID.fromString(TestDataBuilder.RESTAURANT_ID);

    private static final UUID BOOKING_ID = UUID.fromString(TestDataBuilder.BOOKING_ID);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    public void testSave_whenValidInput_thenReturnStatus201() throws Exception {
        BookingDTO bookingDTO = buildBookingDTO();

        when(bookingService.makeBooking(bookingDTO)).thenReturn(bookingDTO);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(bookingDTO.getId().toString()))
                .andExpect(jsonPath("customerId").value(bookingDTO.getCustomerId().toString()))
                .andExpect(jsonPath("restaurantId").value(bookingDTO.getRestaurantId().toString()))
                .andExpect(jsonPath("dateHour").value(bookingDTO.getDateHour().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("customersNo").value(bookingDTO.getCustomersNo()))
                .andExpect(jsonPath("tablesNo").value(bookingDTO.getTablesNo()));
    }

    @Test
    public void testSave_whenInvalidInput_thenReturnStatus400() throws Exception {
        BookingDTO bookingDTO = buildBookingDTO();
        bookingDTO.setCustomerId(null);

        when(bookingService.makeBooking(bookingDTO)).thenReturn(bookingDTO);

        MvcResult mvcResult = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.BAD_REQUEST, "Customer's id is mandatory!");

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindById_whenValidId_thenReturnStatus200() throws Exception {
        BookingDTO bookingDTO = buildBookingDTO();

        when(bookingService.findById(bookingDTO.getId())).thenReturn(bookingDTO);

        mockMvc.perform(get("/bookings/" + bookingDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(bookingDTO.getId().toString()))
                .andExpect(jsonPath("customerId").value(bookingDTO.getCustomerId().toString()))
                .andExpect(jsonPath("restaurantId").value(bookingDTO.getRestaurantId().toString()))
                .andExpect(jsonPath("dateHour").value(bookingDTO.getDateHour().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("customersNo").value(bookingDTO.getCustomersNo()))
                .andExpect(jsonPath("tablesNo").value(bookingDTO.getTablesNo()));
    }

    @Test
    public void testFindById_whenInvalidId_thenReturnStatus400() throws Exception {
        String notFoundMessage = "Booking with id " + BOOKING_ID + " could not be found!";

        when(bookingService.findById(BOOKING_ID)).thenThrow(new NotFoundException(notFoundMessage));

        MvcResult mvcResult = mockMvc.perform(get("/bookings/" + BOOKING_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND, notFoundMessage);

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindAll_returnStatus200() throws Exception {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        BookingDTO bookingDTO = buildBookingDTO();
        bookingDTOList.add(bookingDTO);

        when(bookingService.findAll()).thenReturn(bookingDTOList);

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId().toString()))
                .andExpect(jsonPath("$[0].customerId").value(bookingDTO.getCustomerId().toString()))
                .andExpect(jsonPath("$[0].restaurantId").value(bookingDTO.getRestaurantId().toString()))
                .andExpect(jsonPath("$[0].dateHour").value(bookingDTO.getDateHour().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$[0].customersNo").value(bookingDTO.getCustomersNo()))
                .andExpect(jsonPath("$[0].tablesNo").value(bookingDTO.getTablesNo()));
    }

    @Test
    public void testFindAllByCustomerId_whenValidId_returnStatus200() throws Exception {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        BookingDTO bookingDTO = buildBookingDTO();
        bookingDTOList.add(bookingDTO);

        when(bookingService.findAllByCustomerId(CUSTOMER_ID)).thenReturn(bookingDTOList);

        mockMvc.perform(get("/bookings/customer?id=" + CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId().toString()))
                .andExpect(jsonPath("$[0].customerId").value(bookingDTO.getCustomerId().toString()))
                .andExpect(jsonPath("$[0].restaurantId").value(bookingDTO.getRestaurantId().toString()))
                .andExpect(jsonPath("$[0].dateHour").value(bookingDTO.getDateHour().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$[0].customersNo").value(bookingDTO.getCustomersNo()))
                .andExpect(jsonPath("$[0].tablesNo").value(bookingDTO.getTablesNo()));
    }

    @Test
    public void testFindAllByCustomerId_whenInvalidId_returnStatus400() throws Exception {
        String notFoundMessage = "Customer with id " + CUSTOMER_ID + " could not be found!";

        when(bookingService.findAllByCustomerId(CUSTOMER_ID)).thenThrow(
                new NotFoundException(notFoundMessage)
        );

        MvcResult mvcResult = mockMvc.perform(get("/bookings/customer?id=" + CUSTOMER_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND, notFoundMessage);

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testFindAllByRestaurantId_whenValidId_returnStatus200() throws Exception {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        BookingDTO bookingDTO = buildBookingDTO();
        bookingDTOList.add(bookingDTO);

        when(bookingService.findAllByRestaurantId(RESTAURANT_ID)).thenReturn(bookingDTOList);

        mockMvc.perform(get("/bookings/restaurant?id=" + RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId().toString()))
                .andExpect(jsonPath("$[0].customerId").value(bookingDTO.getCustomerId().toString()))
                .andExpect(jsonPath("$[0].restaurantId").value(bookingDTO.getRestaurantId().toString()))
                .andExpect(jsonPath("$[0].dateHour").value(bookingDTO.getDateHour().format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$[0].customersNo").value(bookingDTO.getCustomersNo()))
                .andExpect(jsonPath("$[0].tablesNo").value(bookingDTO.getTablesNo()));
    }

    @Test
    public void testFindAllByRestaurantId_whenInvalidId_returnStatus400() throws Exception {
        String notFoundMessage = "Restaurant with id " + RESTAURANT_ID + " could not be found!";

        when(bookingService.findAllByRestaurantId(RESTAURANT_ID)).thenThrow(
                new NotFoundException(notFoundMessage)
        );

        MvcResult mvcResult = mockMvc.perform(get("/bookings/restaurant?id=" + RESTAURANT_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND, notFoundMessage);

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void testDelete_whenValidId_thenReturnStatus200() throws Exception {
        mockMvc.perform(delete("/bookings/" + BOOKING_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete_whenInvalidId_thenReturnStatus404() throws Exception {
        String notFoundMessage = "Booking with id " + BOOKING_ID + " could not be found!";

        when(bookingService.findById(BOOKING_ID)).thenThrow(new NotFoundException(notFoundMessage));

        MvcResult mvcResult = mockMvc.perform(get("/bookings/" + BOOKING_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        ApiException expectedException = new ApiException(HttpStatus.NOT_FOUND, notFoundMessage);

        String expectedResponseBody = objectMapper.writeValueAsString(expectedException);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @NotNull
    private BookingDTO buildBookingDTO() {
        BookingDTO bookingDTO = TestDataBuilder.buildBookingDTO(
                RESTAURANT_ID,
                CUSTOMER_ID
        );
        bookingDTO.setId(BOOKING_ID);
        return bookingDTO;
    }
}
