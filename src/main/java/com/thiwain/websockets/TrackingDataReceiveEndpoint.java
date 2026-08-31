package com.thiwain.websockets;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/shipping-tracker")
public class TrackingDataReceiveEndpoint {

    // Maps shipmentId -> set of sessions currently watching that shipment
    private static final Map<String, Set<Session>> shipmentWatchers = new ConcurrentHashMap<>();

    private String shipmentId;

    @OnOpen
    public void onOpen(Session session) {
        this.shipmentId = getQueryParam(session, "id");

        if (shipmentId != null) {
            shipmentWatchers
                    .computeIfAbsent(shipmentId, k -> new CopyOnWriteArraySet<>())
                    .add(session);
        }

        System.out.println("Tracking WebSocket opened for shipment: " + shipmentId);
    }

    @OnClose
    public void onClose(Session session) {
        if (shipmentId != null) {
            Set<Session> watchers = shipmentWatchers.get(shipmentId);
            if (watchers != null) {
                watchers.remove(session);
                if (watchers.isEmpty()) {
                    shipmentWatchers.remove(shipmentId);
                }
            }
        }
        System.out.println("Tracking WebSocket closed for shipment: " + shipmentId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // Called by the POST servlet whenever a shipment status is updated
    public static void broadcastToShipment(String shipmentId, String jsonMessage) {
        Set<Session> watchers = shipmentWatchers.get(shipmentId);
        if (watchers == null) return;

        for (Session session : watchers) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(jsonMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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