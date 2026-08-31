package com.thiwain.servelets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//http://localhost:8081/sc_tracker

@WebServlet("/api/v1/update-shipment")
public class UpdateShipmentStatus extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().println(req.getAttribute("shpId"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
