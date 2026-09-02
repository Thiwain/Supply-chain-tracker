package com.thiwain.dao;

import com.thiwain.entity.Shipment;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class ShipmentDao {

    private final EntityManager em;

    public ShipmentDao(EntityManager em) {
        this.em = em;
    }

    public Optional<Shipment> findById(String shipmentId) {
        return Optional.ofNullable(em.find(Shipment.class, shipmentId));
    }
}