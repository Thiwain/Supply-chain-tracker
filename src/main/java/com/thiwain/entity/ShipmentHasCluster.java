package com.thiwain.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "shipment_has_cluster")
public class ShipmentHasCluster {

    @EmbeddedId
    private ShipmentHasClusterId id = new ShipmentHasClusterId();

    @ManyToOne
    @MapsId("shipmentsId")
    @JoinColumn(name = "shipments_id")
    private Shipment shipment;

    @ManyToOne
    @MapsId("clusterId")
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    public ShipmentHasCluster() {
    }

    public ShipmentHasClusterId getId() {
        return id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Embeddable
    public static class ShipmentHasClusterId implements Serializable {

        @Column(name = "shipments_id", length = 10)
        private String shipmentsId;

        @Column(name = "cluster_id")
        private Integer clusterId;

        public ShipmentHasClusterId() {
        }

        public ShipmentHasClusterId(String shipmentsId, Integer clusterId) {
            this.shipmentsId = shipmentsId;
            this.clusterId = clusterId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ShipmentHasClusterId)) return false;
            ShipmentHasClusterId that = (ShipmentHasClusterId) o;
            return Objects.equals(shipmentsId, that.shipmentsId) && Objects.equals(clusterId, that.clusterId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shipmentsId, clusterId);
        }
    }
}