package com.thiwain.websockets;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/send")
public class SendEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Send socket opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received on /send: " + message);
        ReceiveEndpoint.broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Send socket closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}