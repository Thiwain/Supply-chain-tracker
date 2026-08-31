package com.thiwain.servelets;

import com.thiwain.entity.ShipmentSts;
import com.thiwain.model.ShipmentStatusStage;
import com.thiwain.util.JPAUtil;
import com.thiwain.util.ShipmentStatusProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Vector;

@WebServlet("/shipment-tacking")
public class ShipmentTrackingPgServlet extends HttpServlet {

    private static final int TOTAL_STAGES = 12;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String shipmentId = req.getParameter("id");

        if (shipmentId == null || shipmentId.trim().isEmpty()) {
            req.setAttribute("errorMessage", "No shipment ID was provided.");
            forward(req, resp);
            return;
        }

        req.setAttribute("shipmentId", shipmentId.trim());

        EntityManager em = JPAUtil.getEntityManager();
        int currentStage = 1; // default if no status rows exist yet

        try {
            ShipmentSts latestStatus = em.createQuery(
                            "SELECT s FROM ShipmentSts s WHERE s.shipmentsId = :sid ORDER BY s.datetime DESC",
                            ShipmentSts.class)
                    .setParameter("sid", shipmentId.trim())
                    .setMaxResults(1)
                    .getSingleResult();

            if (latestStatus.getStageNumber() != null) {
                currentStage = latestStatus.getStageNumber();
            }

        } catch (NoResultException e) {
            // No status rows yet — keep default of stage 1
        } finally {
            em.close();
        }

        currentStage = Math.max(1, Math.min(currentStage, TOTAL_STAGES));
        int completionPercentage = (int) Math.round((currentStage / (double) TOTAL_STAGES) * 100);

        Vector<ShipmentStatusStage> stages = ShipmentStatusProvider.getShipmentStatusStages();

        req.setAttribute("statusStages", stages);
        req.setAttribute("currentStage", currentStage);
        req.setAttribute("completionPercentage", completionPercentage);

        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.getRequestDispatcher("/shipment-tracking.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}