<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%-- If opened directly without data, go back to the form --%>
<c:if test="${empty cvData}">
    <c:redirect url="/cvForm.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CV - <c:out value="${cvData.fullName}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-cv">

<div class="toolbar no-print">
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/cvForm.jsp">&larr; Back to Form</a>
    <c:if test="${isSample}"><span class="badge">Sample Template</span></c:if>
    <button class="btn btn-primary" onclick="window.print()">Print / Save as PDF</button>
</div>

<div class="cv-sheet">

    <header class="cv-header">
        <%-- Photo is shown only when the bean has one --%>
        <c:if test="${not empty cvData.photoBase64}">
            <img class="cv-photo" alt="Profile photo"
                 src="<c:out value='${cvData.photoDataUri}'/>">
        </c:if>

        <div class="cv-header-text">
            <h1><c:out value="${cvData.fullName}"/></h1>
            <c:if test="${not empty cvData.professionalTitle}">
                <h2><c:out value="${cvData.professionalTitle}"/></h2>
            </c:if>
            <p class="contact">
                <span><c:out value="${cvData.email}"/></span>
                <c:if test="${not empty cvData.phone}"><span><c:out value="${cvData.phone}"/></span></c:if>
                <c:if test="${not empty cvData.profileUrl}"><span><c:out value="${cvData.profileUrl}"/></span></c:if>
            </p>
        </div>
    </header>

    <c:if test="${not empty cvData.profileSummary}">
        <section>
            <h3>Profile</h3>
            <p><c:out value="${cvData.profileSummary}"/></p>
        </section>
    </c:if>

    <section>
        <h3>Education</h3>
        <div class="entry">
            <div class="entry-head">
                <strong><c:out value="${cvData.highestDegree}"/></strong>
                <c:if test="${not empty cvData.graduationYear}">
                    <span class="date"><c:out value="${cvData.graduationYear}"/></span>
                </c:if>
            </div>
            <c:if test="${not empty cvData.institution}"><div><c:out value="${cvData.institution}"/></div></c:if>
            <c:if test="${not empty cvData.major}"><div class="muted">Major: <c:out value="${cvData.major}"/></div></c:if>
        </div>
    </section>

    <c:if test="${not empty cvData.jobTitle or not empty cvData.company}">
        <section>
            <h3>Work Experience</h3>
            <div class="entry">
                <div class="entry-head">
                    <strong><c:out value="${cvData.jobTitle}"/></strong>
                    <c:if test="${not empty cvData.duration}">
                        <span class="date"><c:out value="${cvData.duration}"/></span>
                    </c:if>
                </div>
                <div><c:out value="${cvData.company}"/></div>
                <ul>
                    <c:forEach var="item" items="${cvData.responsibilitiesList}">
                        <li><c:out value="${item}"/></li>
                    </c:forEach>
                </ul>
            </div>
        </section>
    </c:if>

    <section>
        <h3>Skills</h3>
        <c:if test="${not empty cvData.technicalSkillsList}">
            <p class="skill-label">Technical</p>
            <div class="tags">
                <c:forEach var="s" items="${cvData.technicalSkillsList}">
                    <span class="tag"><c:out value="${s}"/></span>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${not empty cvData.softSkillsList}">
            <p class="skill-label">Soft Skills</p>
            <div class="tags">
                <c:forEach var="s" items="${cvData.softSkillsList}">
                    <span class="tag"><c:out value="${s}"/></span>
                </c:forEach>
            </div>
        </c:if>
    </section>
</div>
</body>
</html>