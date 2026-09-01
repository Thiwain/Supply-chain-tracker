package com.thiwain.servelets;

import com.thiwain.entity.PackageReceiver;
import com.thiwain.entity.Shipment;
import com.thiwain.entity.ShipmentSts;
import com.thiwain.model.ShipmentStatusStage;
import com.thiwain.util.EmailSenderUtil;
import com.thiwain.util.JPAUtil;
import com.thiwain.util.PackageUpdateEmailBody;
import com.thiwain.util.ShipmentStatusProvider;
import com.thiwain.websockets.TrackingDataReceiveEndpoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.NoResultException;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/update-shipment-status")
public class ShipmentStatusUpdateServlet extends HttpServlet {

    private static final int TOTAL_STAGES = 12;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        String requestBody = readRequestBody(req);
        String shipmentsId = extractJsonValue(requestBody, "shipments_id");

        if (shipmentsId == null || shipmentsId.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("shipments_id is required.");
            return;
        }
        shipmentsId = shipmentsId.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            // 1. Find the last recorded stage for this shipment
            int lastStage = 0; // 0 means "no status recorded yet"
            try {
                ShipmentSts latest = em.createQuery(
                                "SELECT s FROM ShipmentSts s WHERE s.shipmentsId = :sid ORDER BY s.datetime DESC",
                                ShipmentSts.class)
                        .setParameter("sid", shipmentsId)
                        .setMaxResults(1)
                        .getSingleResult();

                if (latest.getStageNumber() != null) {
                    lastStage = latest.getStageNumber();
                }
            } catch (NoResultException e) {
                // No previous status — this will be the first one, starting at stage 1
            }

            // 2. Determine the next stage, capped at the final stage
            int nextStage = Math.min(lastStage + 1, TOTAL_STAGES);

            if (lastStage >= TOTAL_STAGES) {
                resp.getWriter().println("Shipment " + shipmentsId + " has already reached the final stage.");
                return;
            }

            // 3. Look up the matching stage name/description automatically
            Vector<ShipmentStatusStage> stages = ShipmentStatusProvider.getShipmentStatusStages();
            ShipmentStatusStage matchedStage = null;
            for (ShipmentStatusStage stage : stages) {
                if (stage.getSequenceOrder() == nextStage) {
                    matchedStage = stage;
                    break;
                }
            }

            if (matchedStage == null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("No matching stage definition found for stage " + nextStage);
                return;
            }

            int isOver = (nextStage == TOTAL_STAGES) ? 1 : 0;

            // 4. Persist the new status row
            ShipmentSts status = new ShipmentSts();
            status.setShipmentsId(shipmentsId);
            status.setDatetime(LocalDateTime.now());
            status.setIsOver(isOver);
            status.setDescription(matchedStage.getDescription());
            status.setStageNumber(nextStage);

            em.getTransaction().begin();
            em.persist(status);
            em.getTransaction().commit();

            // 5. Broadcast the update to any connected WebSocket clients watching this shipment
            int completionPercentage = (int) Math.round((nextStage / (double) TOTAL_STAGES) * 100);

            String json = String.format(
                    "{\"shipmentId\":\"%s\",\"currentStage\":%d,\"completionPercentage\":%d,\"description\":\"%s\"}",
                    shipmentsId, nextStage, completionPercentage,
                    matchedStage.getDescription().replace("\"", "'")
            );

            TrackingDataReceiveEndpoint.broadcastToShipment(shipmentsId, json);

            resp.getWriter().println("Shipment " + shipmentsId + " advanced to stage " + nextStage
                    + " (" + matchedStage.getStatusName() + ")");

            //email sender test
            EmailSenderUtil ems = new EmailSenderUtil();
            String trackingUrl = "http://localhost:8081/sc_tracker/shipment-tacking?id=" + shipmentsId;

            try {
                Shipment sender = em.find(Shipment.class, shipmentsId);

                if (sender != null) {
                    ems.sendEmail(
                            "Your shipment has updated",
                            new PackageUpdateEmailBody().buildShipmentUpdateEmail(
                                    shipmentsId,
                                    trackingUrl,
                                    matchedStage.getStatusName(),
                                    status.getDescription(),
                                    completionPercentage
                            ),
                            sender.getPackageSender().getEmail()
                    );
                }

                PackageReceiver receiver = em.createQuery(
                                "SELECT r FROM PackageReceiver r WHERE r.shipment.id = :sid",
                                PackageReceiver.class)
                        .setParameter("sid", shipmentsId)
                        .getSingleResult();

                if (receiver.getEmail() != null && !receiver.getEmail().isBlank()) {
                    ems.sendEmail(
                            "Your shipment has updated",
                            new PackageUpdateEmailBody().buildShipmentUpdateEmail(
                                    shipmentsId,
                                    trackingUrl,
                                    matchedStage.getStatusName(),
                                    status.getDescription(),
                                    completionPercentage
                            ),
                            receiver.getEmail()
                    );
                }

            } catch (NoResultException e) {
                System.out.println("No receiver record found for shipment " + shipmentsId);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error updating status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        }
        return buffer.toString();
    }

    // Very basic manual JSON extraction — fine for a single expected string field
    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
        Matcher matcher = Pattern.compile(pattern).matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }
}