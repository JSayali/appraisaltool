<%-- 
    Document   : index
    Created on : 13.6.2009, 13:38:03
    Author     : Lukas Strmiska
--%>

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="app-title" /></title>
    </head>
    <body>
        <h1><f:message key="welcome" /></h1>
        <p><f:message key="intro" /></p>
        <ul>
            <c:if test="${!authenticated}">
                <li><a href="login.jsp"><f:message key="login" /></a></li>
            </c:if>
            <security:authorize ifAllGranted="ROLE_SUPERVISOR">
                <li><a href="admin/"><f:message key="admin-tasks" /></a></li>
            </security:authorize>
            <security:authorize ifAllGranted="ROLE_QUALITY_MANAGER">
                <li><a href="qmanager/"><f:message key="qm-tasks" /></a></li>
            </security:authorize>
            <c:if test="${authenticated}">
                <li><a href="appraisal/"><f:message key="conduct-appraisal" /></a></li>
                <li><a href="<c:url value="/j_spring_security_logout"/>"><f:message key="log-off" /></a></li>
            </c:if>
        </ul>
    </body>
</html>
