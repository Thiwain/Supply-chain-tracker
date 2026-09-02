package com.thiwain.dao;

import com.thiwain.entity.ShipmentSts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class ShipmentStsDao {

    private final EntityManager em;

    public ShipmentStsDao(EntityManager em) {
        this.em = em;
    }

    public Optional<ShipmentSts> findLatestByShipmentId(String shipmentsId) {
        try {
            ShipmentSts latest = em.createQuery(
                            "SELECT s FROM ShipmentSts s WHERE s.shipmentsId = :sid ORDER BY s.datetime DESC",
                            ShipmentSts.class)
                    .setParameter("sid", shipmentsId)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.of(latest);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void save(ShipmentSts status) {
        em.persist(status);
    }
}