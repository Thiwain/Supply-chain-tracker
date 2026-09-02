package com.thiwain.dao;

import com.thiwain.entity.PackageReceiver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class PackageReceiverDao {

    private final EntityManager em;

    public PackageReceiverDao(EntityManager em) {
        this.em = em;
    }

    public Optional<PackageReceiver> findByShipmentId(String shipmentId) {
        try {
            PackageReceiver receiver = em.createQuery(
                            "SELECT r FROM PackageReceiver r WHERE r.shipment.id = :sid",
                            PackageReceiver.class)
                    .setParameter("sid", shipmentId)
                    .getSingleResult();
            return Optional.of(receiver);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}