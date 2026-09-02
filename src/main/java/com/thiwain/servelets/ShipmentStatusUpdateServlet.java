package com.thiwain.servelets;

import com.thiwain.dao.PackageReceiverDao;
import com.thiwain.dao.ShipmentDao;
import com.thiwain.dao.ShipmentStsDao;
import com.thiwain.dto.StageAdvanceResult;
import com.thiwain.service.ShipmentNotificationService;
import com.thiwain.service.ShipmentStatusService;
import com.thiwain.util.EmailSenderUtil;
import com.thiwain.util.JPAUtil;
import com.thiwain.util.JsonUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/update-shipment-status")
public class ShipmentStatusUpdateServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ShipmentStatusUpdateServlet.class.getName());

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        String requestBody = JsonUtil.readRequestBody(req);
        String shipmentsId = JsonUtil.extractValue(requestBody, "shipments_id");

        if (shipmentsId == null || shipmentsId.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("shipments_id is required.");
            return;
        }
        shipmentsId = shipmentsId.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            ShipmentStatusService statusService = new ShipmentStatusService(new ShipmentStsDao(em));
            ShipmentNotificationService notificationService = new ShipmentNotificationService(
                    new ShipmentDao(em),
                    new PackageReceiverDao(em),
                    new EmailSenderUtil()
            );

            em.getTransaction().begin();
            StageAdvanceResult result = statusService.advanceToNextStage(shipmentsId);
            em.getTransaction().commit();

            writeResponse(resp, result);

            if (result.getOutcome() == StageAdvanceResult.Outcome.ADVANCED) {
                notificationService.notifyStageAdvanced(result);
            }

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Error updating shipment status for " + shipmentsId, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error updating status: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void writeResponse(HttpServletResponse resp, StageAdvanceResult result) throws IOException {
        switch (result.getOutcome()) {
            case ADVANCED -> resp.getWriter().println(
                    "Shipment " + result.getShipmentId() + " advanced to stage "
                            + result.getStage().getSequenceOrder() + " (" + result.getStage().getStatusName() + ")");
            case ALREADY_COMPLETE -> resp.getWriter().println(
                    "Shipment " + result.getShipmentId() + " has already reached the final stage.");
            case INVALID_SHIPMENT -> {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("No matching stage definition found for shipment " + result.getShipmentId());
            }
        }
    }
}