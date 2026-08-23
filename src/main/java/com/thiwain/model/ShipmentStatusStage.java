package com.thiwain.model;

import java.io.Serializable;

public class ShipmentStatusStage implements Serializable {

    private int sequenceOrder;
    private String statusName;
    private String description;

    public ShipmentStatusStage() {
    }

    public ShipmentStatusStage(int sequenceOrder, String statusName, String description) {
        this.sequenceOrder = sequenceOrder;
        this.statusName = statusName;
        this.description = description;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return sequenceOrder + " - " + statusName;
    }
}