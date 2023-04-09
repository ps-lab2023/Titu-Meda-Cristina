package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "restaurants_capacity")
public class RestaurantCapacity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(name = "tables_no", nullable = false)
    private int tablesNo;
    @Column(name = "customers_no", nullable = false)
    private int customersNo;

    public RestaurantCapacity() {
    }

    public RestaurantCapacity(int tablesNo, int customersNo) {
        this.tablesNo = tablesNo;
        this.customersNo = customersNo;
    }

    public RestaurantCapacity(UUID id, int tablesNo, int customersNo) {
        this.id = id;
        this.tablesNo = tablesNo;
        this.customersNo = customersNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTablesNo() {
        return tablesNo;
    }

    public void setTablesNo(int tablesNo) {
        this.tablesNo = tablesNo;
    }

    public int getCustomersNo() {
        return customersNo;
    }

    public void setCustomersNo(int customersNo) {
        this.customersNo = customersNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantCapacity capacity = (RestaurantCapacity) o;
        return Objects.equals(id, capacity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
