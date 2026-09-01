package com.thiwain.util;

public class PackageUpdateEmailBody {

    public String buildShipmentUpdateEmail(String shipmentId, String trackingUrl,
                                           String statusName, String statusDescription,
                                           int completionPercentage) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<body style=\"margin:0; padding:0; background-color:#eef2ff; font-family:'Segoe UI', Arial, Helvetica, sans-serif;\">"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#eef2ff; padding:48px 0;\">"
                + "<tr><td align=\"center\">"

                + "<table role=\"presentation\" width=\"520\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 20px 40px rgba(15,23,42,0.10); border:1px solid #e2e8f0;\">"

                // Header — solid indigo, modern accent bar
                + "<tr><td style=\"background-color:#6366f1; padding:32px 32px 28px;\">"
                + "<div style=\"color:#e0e7ff; font-size:11px; font-weight:700; text-transform:uppercase; letter-spacing:1px; margin-bottom:6px;\">Tracking Shipment</div>"
                + "<div style=\"color:#ffffff; font-size:24px; font-weight:800; letter-spacing:-0.3px;\">" + shipmentId + "</div>"
                + "</td></tr>"

                // Body
                + "<tr><td style=\"padding:32px;\">"

                + "<h2 style=\"margin:0 0 8px 0; color:#0f172a; font-size:20px; font-weight:700; letter-spacing:-0.2px;\">" + statusName + "</h2>"
                + "<p style=\"margin:0 0 28px 0; color:#64748b; font-size:14px; line-height:1.6;\">"
                + statusDescription
                + "</p>"

                // Progress card
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f8fafc; border-radius:14px; border:1px solid #e2e8f0; margin-bottom:24px;\">"
                + "<tr><td style=\"padding:20px 24px;\">"

                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr>"
                + "<td style=\"color:#64748b; font-size:11px; font-weight:700; text-transform:uppercase; letter-spacing:0.6px;\">Current Status</td>"
                + "<td align=\"right\" style=\"color:#0f172a; font-size:22px; font-weight:800;\">" + completionPercentage + "%</td>"
                + "</tr>"
                + "</table>"

                + "<div style=\"color:#6366f1; font-size:15px; font-weight:700; margin-top:4px; margin-bottom:12px;\">" + statusName + "</div>"

                // Progress bar — nested table pattern for email-safety
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:#e2e8f0; border-radius:999px;\">"
                + "<tr><td style=\"padding:0;\">"
                + "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"" + Math.max(completionPercentage, 4) + "%\" style=\"background-color:#6366f1; border-radius:999px;\">"
                + "<tr><td style=\"height:8px; line-height:8px; font-size:1px;\">&nbsp;</td></tr>"
                + "</table>"
                + "</td></tr>"
                + "</table>"

                + "</td></tr>"
                + "</table>"

                // CTA button
                + "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 auto;\">"
                + "<tr><td align=\"center\" style=\"border-radius:10px; background-color:#6366f1;\">"
                + "<a href=\"" + trackingUrl + "\" target=\"_blank\" "
                + "style=\"display:inline-block; padding:13px 30px; color:#ffffff; text-decoration:none; font-size:14px; font-weight:700; border-radius:10px;\">"
                + "Track My Shipment"
                + "</a>"
                + "</td></tr>"
                + "</table>"

                + "</td></tr>"

                // Footer
                + "<tr><td style=\"padding:22px 32px; background-color:#f8fafc; border-top:1px solid #e2e8f0;\">"
                + "<p style=\"margin:0; color:#94a3b8; font-size:12px; text-align:center; letter-spacing:0.1px;\">"
                + "This is an automated notification from SC Tracker. Please do not reply to this email."
                + "</p>"
                + "</td></tr>"

                + "</table>"

                + "</td></tr>"
                + "</table>"
                + "</body>"
                + "</html>";
    }
}