package com.thiwain.service;

import com.thiwain.dao.ShipmentStsDao;
import com.thiwain.dto.StageAdvanceResult;
import com.thiwain.entity.ShipmentSts;
import com.thiwain.model.ShipmentStatusStage;
import com.thiwain.util.ShipmentStatusProvider;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Vector;

public class ShipmentStatusService {

    private static final int TOTAL_STAGES = 12;

    private final ShipmentStsDao shipmentStsDao;

    public ShipmentStatusService(ShipmentStsDao shipmentStsDao) {
        this.shipmentStsDao = shipmentStsDao;
    }

    /**
     * Advances the given shipment to its next status stage, persists it,
     * and returns a result describing what happened. Does not touch
     * HTTP, WebSockets, or email — callers decide what to do with the result.
     */
    public StageAdvanceResult advanceToNextStage(String shipmentId) {
        int lastStage = shipmentStsDao.findLatestByShipmentId(shipmentId)
                .map(ShipmentSts::getStageNumber)
                .orElse(0);

        if (lastStage >= TOTAL_STAGES) {
            return StageAdvanceResult.alreadyComplete(shipmentId);
        }

        int nextStage = lastStage + 1;

        Optional<ShipmentStatusStage> matchedStage = findStageDefinition(nextStage);
        if (matchedStage.isEmpty()) {
            return StageAdvanceResult.invalidShipment(shipmentId);
        }

        ShipmentStatusStage stageDefinition = matchedStage.get();

        ShipmentSts newStatus = new ShipmentSts();
        newStatus.setShipmentsId(shipmentId);
        newStatus.setDatetime(LocalDateTime.now());
        newStatus.setIsOver(nextStage == TOTAL_STAGES ? 1 : 0);
        newStatus.setDescription(stageDefinition.getDescription());
        newStatus.setStageNumber(nextStage);

        shipmentStsDao.save(newStatus);

        int completionPercentage = calculatePercentage(nextStage);

        return StageAdvanceResult.advanced(shipmentId, stageDefinition, completionPercentage);
    }

    private Optional<ShipmentStatusStage> findStageDefinition(int stageNumber) {
        Vector<ShipmentStatusStage> stages = ShipmentStatusProvider.getShipmentStatusStages();
        return stages.stream()
                .filter(s -> s.getSequenceOrder() == stageNumber)
                .findFirst();
    }

    private int calculatePercentage(int stageNumber) {
        return (int) Math.round((stageNumber / (double) TOTAL_STAGES) * 100);
    }
}