package com.thiwain.servelets;

import com.thiwain.entity.ShipmentSts;
import com.thiwain.model.ShipmentStatusStage;
import com.thiwain.util.JPAUtil;
import com.thiwain.util.ShipmentStatusProvider;
import com.thiwain.websockets.TrackingDataReceiveEndpoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Vector;

@WebServlet("/update-shipment-status")
public class ShipmentStatusUpdateServlet extends HttpServlet {

    private static final int TOTAL_STAGES = 12;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        String shipmentsId = req.getParameter("shipments_id");

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
}