<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Shipment Tracking</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css"/>
</head>
<body>

<c:if test="${not empty errorMessage}">
    <div class="container mt-5">
        <div class="alert alert-danger">${errorMessage}</div>
    </div>
</c:if>

<c:if test="${not empty shipmentId}">
    <div class="timeline-wrapper">

        <h4 class="text-center mb-1">Shipment ${shipmentId}</h4>
        <p id="connectionStatus" class="connection-status">Connecting...</p>

        <div class="progress-badge">
            <div class="percentage" id="percentageValue">${completionPercentage}%</div>
            <div class="label">Completed</div>
        </div>

        <div class="timeline">
            <c:forEach var="stage" items="${statusStages}">
                <div class="timeline-item ${stage.sequenceOrder < currentStage ? 'completed' : (stage.sequenceOrder == currentStage ? 'current' : '')}"
                     data-sequence="${stage.sequenceOrder}">
                    <div class="timeline-dot"></div>
                    <div class="stage-name">${stage.statusName}</div>
                    <div class="stage-description">${stage.description}</div>
                </div>
            </c:forEach>
        </div>

    </div>

    <script>
        const shipmentId = "${shipmentId}";
        const initialStage = ${currentStage};

        const protocol = window.location.protocol === "https:" ? "wss://" : "ws://";
        const host = window.location.host;
        const contextPath = "<%= request.getContextPath() %>";

        const socket = new WebSocket(
            protocol + host + contextPath + "/shipping-tracker?id=" + encodeURIComponent(shipmentId)
                + "&currentStage=" + initialStage
        );

        console.log(protocol + host + contextPath + "/shipping-tracker?id=" + encodeURIComponent(shipmentId)
                                    + "&currentStage=" + initialStage);

        const statusEl = document.getElementById("connectionStatus");
        const percentageEl = document.getElementById("percentageValue");

        socket.onopen = () => {
            statusEl.textContent = "Live tracking connected";
            statusEl.className = "connection-status connected";
        };

        socket.onclose = () => {
            statusEl.textContent = "Tracking connection closed";
            statusEl.className = "connection-status disconnected";
        };

        socket.onerror = (e) => {
            console.error("Tracking socket error", e);
            statusEl.textContent = "Connection error";
            statusEl.className = "connection-status disconnected";
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            updateTimeline(data.currentStage, data.completionPercentage);
        };

        function updateTimeline(currentStage, completionPercentage) {
            percentageEl.textContent = completionPercentage + "%";

            document.querySelectorAll(".timeline-item").forEach((item) => {
                const sequence = parseInt(item.getAttribute("data-sequence"), 10);
                item.classList.remove("completed", "current");

                if (sequence < currentStage) {
                    item.classList.add("completed");
                } else if (sequence === currentStage) {
                    item.classList.add("current");
                }
            });
        }
    </script>
</c:if>

</body>
</html>