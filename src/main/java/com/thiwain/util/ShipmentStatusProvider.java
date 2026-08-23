package com.thiwain.util;

import com.thiwain.model.ShipmentStatusStage;

import java.util.Vector;

public class ShipmentStatusProvider {

    public static Vector<ShipmentStatusStage> getShipmentStatusStages() {
        Vector<ShipmentStatusStage> stages = new Vector<>();

        stages.add(new ShipmentStatusStage(1, "Processing",
                "The order has been received and is being prepared for collection."));

        stages.add(new ShipmentStatusStage(2, "Order Collected",
                "The order has been collected from the sender's location."));

        stages.add(new ShipmentStatusStage(3, "Arrived at Warehouse",
                "The shipment has arrived at the origin warehouse."));

        stages.add(new ShipmentStatusStage(4, "Dispatched to Customs",
                "The shipment has been forwarded to customs for export processing."));

        stages.add(new ShipmentStatusStage(5, "Customs Clearance in Progress (Origin)",
                "The shipment is undergoing customs clearance in the country of origin."));

        stages.add(new ShipmentStatusStage(6, "Arrived at Airport",
                "The shipment has arrived at the departure airport."));

        stages.add(new ShipmentStatusStage(7, "In Transit",
                "The shipment is currently in transit via air freight."));

        stages.add(new ShipmentStatusStage(8, "Arrived in Destination Country",
                "The shipment has arrived in the destination country."));

        stages.add(new ShipmentStatusStage(9, "Arrived at Customs",
                "The shipment has reached the destination customs facility."));

        stages.add(new ShipmentStatusStage(10, "Customs Clearance in Progress (Destination)",
                "The shipment is undergoing customs clearance in the destination country."));

        stages.add(new ShipmentStatusStage(11, "Collected by Courier",
                "The shipment has been collected by the local courier for final delivery."));

        stages.add(new ShipmentStatusStage(12, "Order Delivered",
                "The order has been successfully delivered to the customer."));

        return stages;
    }
}

/*
Vector<ShipmentStatusStage> stages = ShipmentStatusProvider.getShipmentStatusStages();
for (ShipmentStatusStage stage : stages) {
    System.out.println(stage.getSequenceOrder() + ". " + stage.getStatusName()
            + " - " + stage.getDescription());
}
* */