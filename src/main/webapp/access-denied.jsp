<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Access Denied</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5 text-center">
    <h2 class="text-danger">403 - Access Denied</h2>
    <p>${errorMessage}</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
</div>
</body>
</html>