package com.thiwain.service;

import com.thiwain.dao.PackageReceiverDao;
import com.thiwain.dao.ShipmentDao;
import com.thiwain.dto.StageAdvanceResult;
import com.thiwain.entity.PackageReceiver;
import com.thiwain.entity.Shipment;
import com.thiwain.util.EmailSenderUtil;
import com.thiwain.util.JsonUtil;
import com.thiwain.util.PackageUpdateEmailBody;
import com.thiwain.websockets.TrackingDataReceiveEndpoint;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipmentNotificationService {

    private static final Logger LOGGER = Logger.getLogger(ShipmentNotificationService.class.getName());
    private static final String TRACKING_BASE_URL = "http://localhost:8081/sc_tracker/shipment-tacking?id=";

    private final ShipmentDao shipmentDao;
    private final PackageReceiverDao packageReceiverDao;
    private final EmailSenderUtil emailSender;

    public ShipmentNotificationService(ShipmentDao shipmentDao,
                                       PackageReceiverDao packageReceiverDao,
                                       EmailSenderUtil emailSender) {
        this.shipmentDao = shipmentDao;
        this.packageReceiverDao = packageReceiverDao;
        this.emailSender = emailSender;
    }

    public void notifyStageAdvanced(StageAdvanceResult result) {
        broadcastWebSocketUpdate(result);
        sendSenderEmail(result);
        sendReceiverEmail(result);
    }

    private void broadcastWebSocketUpdate(StageAdvanceResult result) {
        String json = JsonUtil.buildShipmentUpdateJson(
                result.getShipmentId(),
                result.getStage().getSequenceOrder(),
                result.getCompletionPercentage(),
                result.getStage().getDescription()
        );
        TrackingDataReceiveEndpoint.broadcastToShipment(result.getShipmentId(), json);
    }

    private void sendSenderEmail(StageAdvanceResult result) {
        try {
            Optional<Shipment> shipment = shipmentDao.findById(result.getShipmentId());
            if (shipment.isEmpty()) return;

            String senderEmail = shipment.get().getPackageSender().getEmail();
            if (isBlank(senderEmail)) return;

            emailSender.sendEmail(
                    buildSubject(result),
                    buildBody(result),
                    senderEmail
            );
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Failed to send sender notification for " + result.getShipmentId(), e);
        }
    }

    private void sendReceiverEmail(StageAdvanceResult result) {
        try {
            Optional<PackageReceiver> receiver = packageReceiverDao.findByShipmentId(result.getShipmentId());
            if (receiver.isEmpty() || isBlank(receiver.get().getEmail())) return;

            emailSender.sendEmail(
                    buildSubject(result),
                    buildBody(result),
                    receiver.get().getEmail()
            );
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Failed to send receiver notification for " + result.getShipmentId(), e);
        }
    }

    private String buildSubject(StageAdvanceResult result) {
        return "Shipment Update: " + result.getStage().getStatusName();
    }

    private String buildBody(StageAdvanceResult result) {
        return new PackageUpdateEmailBody().buildShipmentUpdateEmail(
                result.getShipmentId(),
                TRACKING_BASE_URL + result.getShipmentId(),
                result.getStage().getStatusName(),
                result.getStage().getDescription(),
                result.getCompletionPercentage()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}