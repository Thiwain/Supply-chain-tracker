package com.thiwain.dto;

import com.thiwain.model.ShipmentStatusStage;

public class StageAdvanceResult {

    public enum Outcome {ADVANCED, ALREADY_COMPLETE, INVALID_SHIPMENT}

    private final Outcome outcome;
    private final String shipmentId;
    private final ShipmentStatusStage stage;
    private final int completionPercentage;

    private StageAdvanceResult(Outcome outcome, String shipmentId,
                               ShipmentStatusStage stage, int completionPercentage) {
        this.outcome = outcome;
        this.shipmentId = shipmentId;
        this.stage = stage;
        this.completionPercentage = completionPercentage;
    }

    public static StageAdvanceResult advanced(String shipmentId, ShipmentStatusStage stage, int completionPercentage) {
        return new StageAdvanceResult(Outcome.ADVANCED, shipmentId, stage, completionPercentage);
    }

    public static StageAdvanceResult alreadyComplete(String shipmentId) {
        return new StageAdvanceResult(Outcome.ALREADY_COMPLETE, shipmentId, null, 100);
    }

    public static StageAdvanceResult invalidShipment(String shipmentId) {
        return new StageAdvanceResult(Outcome.INVALID_SHIPMENT, shipmentId, null, 0);
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatusStage getStage() {
        return stage;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }
}