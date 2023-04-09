package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "menus")
public class Menu {
    @Id
    private UUID id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "content", nullable = false)
    @Lob
    private byte[] content;
    @Column(name = "content_type", nullable = false)
    private String contentType;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(String fileName, byte[] content, String contentType, Restaurant restaurant) {
        this.fileName = fileName;
        this.content = content;
        this.contentType = contentType;
        this.setRestaurant(restaurant);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurant.setMenu(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'';
    }
}
