package com.thiwain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_sts")
public class ShipmentSts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Still not in your original schema — add via ALTER TABLE if not present

    @Column(name = "shipments_id", length = 10)
    private String shipmentsId;

    private LocalDateTime datetime;

    @Column(name = "is_over")
    private Integer isOver;

    @Column(length = 255)
    private String description;

    @Column(name = "shipment_sts")
    private Integer stageNumber;

    public ShipmentSts() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipmentsId() {
        return shipmentsId;
    }

    public void setShipmentsId(String shipmentsId) {
        this.shipmentsId = shipmentsId;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getIsOver() {
        return isOver;
    }

    public void setIsOver(Integer isOver) {
        this.isOver = isOver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(Integer stageNumber) {
        this.stageNumber = stageNumber;
    }
}