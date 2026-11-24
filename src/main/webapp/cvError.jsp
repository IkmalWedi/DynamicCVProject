<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CV Builder - Validation Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-form">
<div class="form-container error-box">
    <h1>&#9888; Unable to Generate CV</h1>
    <p><c:out value="${empty errorMessage ? 'Invalid input.' : errorMessage}"/></p>

    <ul class="error-list">
        <c:forEach var="err" items="${errorList}">
            <li><c:out value="${err}"/></li>
        </c:forEach>
    </ul>

    <div class="actions">
        <a class="btn btn-secondary" href="javascript:history.back()">&larr; Go Back (keep my entries)</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/cvForm.jsp">Start Over</a>
    </div>
</div>
</body>
</html>