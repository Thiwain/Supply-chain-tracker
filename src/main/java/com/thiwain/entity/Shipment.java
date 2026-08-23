package com.thiwain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    // VARCHAR(10) primary key, assigned manually by the application (not auto-generated)
    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne
    @JoinColumn(name = "package_sender_id", nullable = false)
    private PackageSender packageSender;

    @ManyToOne
    @JoinColumn(name = "collected_branch_id", nullable = false)
    private Branch collectedBranch;

    private LocalDateTime collectedDate;

    private LocalDateTime estimatedDate;

    private Integer isActive;

    private Double totalFee;

    private Double distance;

    public Shipment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PackageSender getPackageSender() {
        return packageSender;
    }

    public void setPackageSender(PackageSender packageSender) {
        this.packageSender = packageSender;
    }

    public Branch getCollectedBranch() {
        return collectedBranch;
    }

    public void setCollectedBranch(Branch collectedBranch) {
        this.collectedBranch = collectedBranch;
    }

    public LocalDateTime getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(LocalDateTime collectedDate) {
        this.collectedDate = collectedDate;
    }

    public LocalDateTime getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(LocalDateTime estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}