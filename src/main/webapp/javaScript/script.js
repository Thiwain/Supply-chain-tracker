const sendWsReq = function() {
    const text = "helloo";
    if (text && sendSocket.readyState === WebSocket.OPEN) {
        sendSocket.send(text);
    }
};

//setInterval(sendWsReq, 2000);