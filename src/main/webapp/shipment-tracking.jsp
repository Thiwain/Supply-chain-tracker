<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Shipment Tracking</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --primary-dark: #4f46e5;
            --primary-light: #eef2ff;
            --success: #10b981;
            --success-light: #ecfdf5;
            --slate-900: #0f172a;
            --slate-700: #334155;
            --slate-500: #64748b;
            --slate-300: #cbd5e1;
            --slate-200: #e2e8f0;
            --slate-100: #f1f5f9;
            --slate-50: #f8fafc;
            --danger: #ef4444;
        }

        * { box-sizing: border-box; }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            background: linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
            min-height: 100vh;
            margin: 0;
            color: var(--slate-900);
        }

        .page-wrapper {
            max-width: 640px;
            margin: 0 auto;
            padding: 56px 20px 80px;
        }

        /* --- Card shell --- */
        .tracking-card {
            background: #ffffff;
            border-radius: 20px;
            box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04), 0 20px 40px -12px rgba(15, 23, 42, 0.10);
            overflow: hidden;
            border: 1px solid var(--slate-200);
        }

        /* --- Header --- */
        .card-header-custom {
            background: linear-gradient(135deg, var(--primary) 0%, #8b5cf6 100%);
            padding: 36px 32px 32px;
            color: #fff;
            position: relative;
        }

        .card-header-custom::after {
            content: '';
            position: absolute;
            inset: 0;
            background: radial-gradient(circle at top right, rgba(255,255,255,0.15), transparent 60%);
        }

        .shipment-label {
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            opacity: 0.85;
            margin-bottom: 4px;
            position: relative;
        }

        .shipment-id {
            font-size: 1.6rem;
            font-weight: 800;
            letter-spacing: -0.01em;
            position: relative;
        }

        .connection-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: rgba(255, 255, 255, 0.18);
            backdrop-filter: blur(6px);
            padding: 5px 12px 5px 10px;
            border-radius: 999px;
            font-size: 0.75rem;
            font-weight: 600;
            margin-top: 14px;
            position: relative;
        }

        .connection-dot {
            width: 7px;
            height: 7px;
            border-radius: 50%;
            background: #fbbf24;
            box-shadow: 0 0 0 3px rgba(251, 191, 36, 0.3);
            animation: pulse 1.6s infinite;
        }

        .connection-badge.connected .connection-dot {
            background: #4ade80;
            box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.3);
        }

        .connection-badge.disconnected .connection-dot {
            background: #f87171;
            box-shadow: 0 0 0 3px rgba(248, 113, 113, 0.3);
            animation: none;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.5; }
        }

        /* --- Progress section --- */
        .progress-section {
            padding: 28px 32px 8px;
        }

        .progress-top-row {
            display: flex;
            justify-content: space-between;
            align-items: baseline;
            margin-bottom: 10px;
        }

        .progress-percentage {
            font-size: 2.25rem;
            font-weight: 800;
            color: var(--slate-900);
            letter-spacing: -0.02em;
            transition: color 0.3s ease;
        }

        .progress-caption {
            font-size: 0.8rem;
            font-weight: 500;
            color: var(--slate-500);
        }

        .progress-track {
            width: 100%;
            height: 8px;
            background: var(--slate-100);
            border-radius: 999px;
            overflow: hidden;
        }

        .progress-fill {
            height: 100%;
            border-radius: 999px;
            background: linear-gradient(90deg, var(--primary) 0%, #8b5cf6 100%);
            transition: width 0.5s cubic-bezier(0.4, 0, 0.2, 1);
        }

        /* --- Timeline --- */
        .timeline {
            padding: 24px 32px 36px;
        }

        .timeline-item {
            position: relative;
            padding-left: 44px;
            padding-bottom: 28px;
            display: flex;
            flex-direction: column;
        }

        .timeline-item:last-child {
            padding-bottom: 0;
        }

        .timeline-item::before {
            content: '';
            position: absolute;
            left: 13px;
            top: 26px;
            bottom: -4px;
            width: 2px;
            background: var(--slate-200);
            transition: background-color 0.4s ease;
        }

        .timeline-item:last-child::before {
            display: none;
        }

        .timeline-dot {
            position: absolute;
            left: 0;
            top: 0;
            width: 27px;
            height: 27px;
            border-radius: 50%;
            background: #fff;
            border: 2px solid var(--slate-300);
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.35s ease;
            z-index: 1;
        }

        .timeline-dot::after {
            content: '';
            width: 7px;
            height: 7px;
            border-radius: 50%;
            background: var(--slate-300);
            transition: all 0.35s ease;
        }

        .timeline-item.completed::before {
            background: var(--success);
        }

        .timeline-item.completed .timeline-dot {
            background: var(--success);
            border-color: var(--success);
        }

        .timeline-item.completed .timeline-dot::after {
            content: '✓';
            background: none;
            color: #fff;
            font-size: 0.7rem;
            font-weight: 800;
            width: auto;
            height: auto;
        }

        .timeline-item.current .timeline-dot {
            background: var(--primary);
            border-color: var(--primary);
            box-shadow: 0 0 0 5px var(--primary-light);
        }

        .timeline-item.current .timeline-dot::after {
            background: #fff;
        }

        .stage-name {
            font-weight: 600;
            font-size: 0.95rem;
            color: var(--slate-300);
            margin-bottom: 2px;
            margin-top: 3px;
            transition: color 0.35s ease;
        }

        .timeline-item.completed .stage-name {
            color: var(--slate-700);
        }

        .timeline-item.current .stage-name {
            color: var(--slate-900);
        }

        .stage-description {
            font-size: 0.82rem;
            color: var(--slate-300);
            line-height: 1.5;
            transition: color 0.35s ease;
        }

        .timeline-item.completed .stage-description {
            color: var(--slate-500);
        }

        .timeline-item.current .stage-description {
            color: var(--slate-500);
        }

        .current-badge {
            display: inline-block;
            font-size: 0.65rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: var(--primary-dark);
            background: var(--primary-light);
            padding: 2px 8px;
            border-radius: 999px;
            margin-top: 6px;
            width: fit-content;
        }

        /* --- Error state --- */
        .error-card {
            background: #fff;
            border: 1px solid #fecaca;
            border-radius: 16px;
            padding: 24px 28px;
            display: flex;
            align-items: flex-start;
            gap: 14px;
            box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04);
        }

        .error-icon {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: #fef2f2;
            color: var(--danger);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            flex-shrink: 0;
        }

        .error-text {
            color: var(--slate-700);
            font-size: 0.9rem;
            padding-top: 6px;
        }
    </style>
</head>
<body>

<c:if test="${not empty errorMessage}">
    <div class="page-wrapper">
        <div class="error-card">
            <div class="error-icon">!</div>
            <div class="error-text">${errorMessage}</div>
        </div>
    </div>
</c:if>

<c:if test="${not empty shipmentId}">
    <div class="page-wrapper">
        <div class="tracking-card">

            <div class="card-header-custom">
                <div class="shipment-label">Tracking Shipment</div>
                <div class="shipment-id">${shipmentId}</div>
                <div id="connectionStatus" class="connection-badge">
                    <span class="connection-dot"></span>
                    <span class="connection-text">Connecting...</span>
                </div>
            </div>

            <div class="progress-section">
                <div class="progress-top-row">
                    <span class="progress-percentage" id="percentageValue">${completionPercentage}%</span>
                    <span class="progress-caption">Complete</span>
                </div>
                <div class="progress-track">
                    <div class="progress-fill" id="progressFill" style="width: ${completionPercentage}%;"></div>
                </div>
            </div>

            <div class="timeline">
                <c:forEach var="stage" items="${statusStages}">
                    <div class="timeline-item ${stage.sequenceOrder < currentStage ? 'completed' : (stage.sequenceOrder == currentStage ? 'current' : '')}"
                         data-sequence="${stage.sequenceOrder}">
                        <div class="timeline-dot"></div>
                        <div class="stage-name">${stage.statusName}</div>
                        <div class="stage-description">${stage.description}</div>
                        <c:if test="${stage.sequenceOrder == currentStage}">
                            <span class="current-badge">In Progress</span>
                        </c:if>
                    </div>
                </c:forEach>
            </div>

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

        const statusEl = document.getElementById("connectionStatus");
        const statusTextEl = statusEl.querySelector(".connection-text");
        const percentageEl = document.getElementById("percentageValue");
        const progressFillEl = document.getElementById("progressFill");

        socket.onopen = () => {
            statusTextEl.textContent = "Live";
            statusEl.className = "connection-badge connected";
        };

        socket.onclose = () => {
            statusTextEl.textContent = "Disconnected";
            statusEl.className = "connection-badge disconnected";
        };

        socket.onerror = (e) => {
            console.error("Tracking socket error", e);
            statusTextEl.textContent = "Connection error";
            statusEl.className = "connection-badge disconnected";
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            updateTimeline(data.currentStage, data.completionPercentage);
        };

        function updateTimeline(currentStage, completionPercentage) {
            percentageEl.textContent = completionPercentage + "%";
            progressFillEl.style.width = completionPercentage + "%";

            document.querySelectorAll(".timeline-item").forEach((item) => {
                const sequence = parseInt(item.getAttribute("data-sequence"), 10);
                item.classList.remove("completed", "current");

                const existingBadge = item.querySelector(".current-badge");
                if (existingBadge) existingBadge.remove();

                if (sequence < currentStage) {
                    item.classList.add("completed");
                } else if (sequence === currentStage) {
                    item.classList.add("current");
                    const badge = document.createElement("span");
                    badge.className = "current-badge";
                    badge.textContent = "In Progress";
                    item.appendChild(badge);
                }
            });
        }
    </script>
</c:if>

</body>
</html>