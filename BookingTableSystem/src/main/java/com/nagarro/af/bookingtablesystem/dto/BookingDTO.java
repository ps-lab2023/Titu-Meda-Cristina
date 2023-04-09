package com.nagarro.af.bookingtablesystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class BookingDTO {
    @Id
    private UUID id;
    @NotNull(message = "Customer's id is mandatory!")
    private UUID customerId;
    @NotNull(message = "Restaurant's id is mandatory!")
    private UUID restaurantId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateHour;
    @NotNull(message = "The number of customers for the reservation is mandatory!")
    @Positive
    private Integer customersNo;
    @Positive
    @NotNull(message = "The number of booked tables is mandatory!")
    private Integer tablesNo;

    public BookingDTO() {
    }

    public BookingDTO(UUID customerId, UUID restaurantId, LocalDateTime dateHour, Integer customersNo, Integer tablesNo) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.dateHour = dateHour;
        this.customersNo = customersNo;
        this.tablesNo = tablesNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getDateHour() {
        return dateHour;
    }

    public void setDateHour(LocalDateTime dateHour) {
        this.dateHour = dateHour;
    }

    public Integer getCustomersNo() {
        return customersNo;
    }

    public void setCustomersNo(Integer customersNo) {
        this.customersNo = customersNo;
    }

    public Integer getTablesNo() {
        return tablesNo;
    }

    public void setTablesNo(Integer tablesNo) {
        this.tablesNo = tablesNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDTO that = (BookingDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
