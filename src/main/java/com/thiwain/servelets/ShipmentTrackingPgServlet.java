package com.thiwain.servelets;

import com.thiwain.model.ShipmentStatusStage;
import com.thiwain.util.ShipmentStatusProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

@WebServlet("/shipment-tacking")
public class ShipmentTrackingPgServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String shipmentId = req.getParameter("id");

        if (shipmentId == null || shipmentId.trim().isEmpty()) {
            req.setAttribute("errorMessage", "No shipment ID was provided.");
            forward(req, resp);
            return;
        }

        req.setAttribute("shipmentId", shipmentId.trim());

        Vector<ShipmentStatusStage> stages = ShipmentStatusProvider.getShipmentStatusStages();

        // Determine how many stages are "completed" via ?currentStage= param (defaults to stage 1)
        int currentStage = 1;
        String currentStageParam = req.getParameter("currentStage");
        if (currentStageParam != null) {
            try {
                currentStage = Integer.parseInt(currentStageParam.trim());
            } catch (NumberFormatException ignored) {
                // fall back to default
            }
        }
        currentStage = Math.max(1, Math.min(currentStage, stages.size()));

        int completionPercentage = (int) Math.round((currentStage / (double) stages.size()) * 100);

        // Reverse so most recent/current stage appears first (descending order)
        Vector<ShipmentStatusStage> descendingStages = new Vector<>(stages);
        Collections.reverse(descendingStages);

        req.setAttribute("statusStages", descendingStages);
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