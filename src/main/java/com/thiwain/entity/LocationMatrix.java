package com.thiwain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "location_matrix")
public class LocationMatrix {

    @Id
    private Integer id;

    @Column(length = 100)
    private String x;

    @Column(length = 100)
    private String y;

    public LocationMatrix() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}