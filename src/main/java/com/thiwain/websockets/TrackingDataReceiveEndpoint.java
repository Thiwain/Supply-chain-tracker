package com.thiwain.websockets;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/shipping-tracker")
public class TrackingDataReceiveEndpoint {

    private static final int TOTAL_STAGES = 12;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    private ScheduledFuture<?> simulationTask;

    @OnOpen
    public void onOpen(Session session) {
        String shipmentId = getQueryParam(session, "id");
        String startParam = getQueryParam(session, "currentStage");

        int startingStage = 1;
        if (startParam != null) {
            try {
                startingStage = Integer.parseInt(startParam);
            } catch (NumberFormatException ignored) {
                // fall back to default
            }
        }
        final int initialStage = Math.max(1, Math.min(startingStage, TOTAL_STAGES));
        final int[] stageHolder = {initialStage};

        System.out.println("Tracking WebSocket opened for shipment: " + shipmentId);

        // Simulated progression: advances one stage every 4 seconds until delivery
        simulationTask = scheduler.scheduleAtFixedRate(() -> {
            if (!session.isOpen()) return;

            int stage = stageHolder[0];
            int percentage = (int) Math.round((stage / (double) TOTAL_STAGES) * 100);

            String json = String.format(
                    "{\"shipmentId\":\"%s\",\"currentStage\":%d,\"completionPercentage\":%d}",
                    shipmentId, stage, percentage
            );

            try {
                session.getBasicRemote().sendText(json);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (stage >= TOTAL_STAGES) {
                simulationTask.cancel(false);
            } else {
                stageHolder[0] = stage + 1;
            }
        }, 0, 4, TimeUnit.SECONDS);
    }

    @OnClose
    public void onClose(Session session) {
        if (simulationTask != null) {
            simulationTask.cancel(true);
        }
        System.out.println("Tracking WebSocket closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private String getQueryParam(Session session, String key) {
        String query = session.getRequestURI().getQuery();
        if (query == null) return null;

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2 && parts[0].equals(key)) {
                return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}