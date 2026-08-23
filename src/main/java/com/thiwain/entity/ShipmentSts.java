package com.thiwain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_sts")
public class ShipmentSts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Not in original schema — added because JPA requires a primary key

    @ManyToOne
    @JoinColumn(name = "shipments_id")
    private Shipment shipment;

    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private Integer isOver;

    public ShipmentSts() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Integer getIsOver() {
        return isOver;
    }

    public void setIsOver(Integer isOver) {
        this.isOver = isOver;
    }
}