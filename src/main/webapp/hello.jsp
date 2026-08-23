<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocket Demo</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 500px; margin: 40px auto; }
        #messageInput { width: 70%; padding: 8px; }
        button { padding: 8px 16px; }
        #messages { list-style: none; padding: 0; margin-top: 20px; }
        #messages li { background: #f0f0f0; margin: 4px 0; padding: 8px; border-radius: 4px; }
        #status { font-size: 0.9em; color: gray; }
    </style>
</head>
<body>
    <h1>WebSocket Demo</h1>
    <p id="status">Connecting...</p>

    <input type="text" id="messageInput" placeholder="Type a message..." />
    <button id="sendBtn">Send</button>

    <ul id="messages"></ul>

    <script>
        const protocol = window.location.protocol === "https:" ? "wss://" : "ws://";
        const host = window.location.host;
        const contextPath = "<%= request.getContextPath() %>";

        const sendSocket = new WebSocket(protocol + host + contextPath + "/send");
        const receiveSocket = new WebSocket(protocol + host + contextPath + "/receive");

        const statusEl = document.getElementById("status");
        const messagesEl = document.getElementById("messages");
        const inputEl = document.getElementById("messageInput");
        const sendBtn = document.getElementById("sendBtn");

        let sendReady = false;
        let receiveReady = false;

        function updateStatus() {
            statusEl.textContent = (sendReady && receiveReady) ? "Connected" : "Connecting...";
        }

        sendSocket.onopen = () => { sendReady = true; updateStatus(); };
        receiveSocket.onopen = () => { receiveReady = true; updateStatus(); };

        sendSocket.onerror = (e) => console.error("Send socket error", e);
        receiveSocket.onerror = (e) => console.error("Receive socket error", e);

        sendSocket.onclose = () => { sendReady = false; updateStatus(); };
        receiveSocket.onclose = () => { receiveReady = false; updateStatus(); };

        receiveSocket.onmessage = (event) => {
            const li = document.createElement("li");
            li.textContent = event.data;
            messagesEl.appendChild(li);
        };

        function sendMessage() {
            const text = inputEl.value.trim();
            if (text && sendSocket.readyState === WebSocket.OPEN) {
                sendSocket.send(text);
                inputEl.value = "";
            }
        }

        sendBtn.addEventListener("click", sendMessage);
        inputEl.addEventListener("keydown", (e) => {
            if (e.key === "Enter") sendMessage();
        });
    </script>

    <script src="javaScript/script.js"></script>
</body>
</html>