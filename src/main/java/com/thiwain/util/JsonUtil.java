package com.thiwain.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {

    public static String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        }
        return buffer.toString();
    }

    // Basic single-field extraction — sufficient for simple flat JSON payloads.
    // Replace with a real JSON library (e.g. Jackson) if payloads grow more complex.
    public static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
        Matcher matcher = Pattern.compile(pattern).matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String buildShipmentUpdateJson(String shipmentId, int currentStage,
                                                 int completionPercentage, String description) {
        return String.format(
                "{\"shipmentId\":\"%s\",\"currentStage\":%d,\"completionPercentage\":%d,\"description\":\"%s\"}",
                shipmentId, currentStage, completionPercentage,
                description != null ? description.replace("\"", "'") : ""
        );
    }
}