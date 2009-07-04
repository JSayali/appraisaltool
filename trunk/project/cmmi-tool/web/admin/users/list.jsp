<%-- 
    Document   : userList
    Created on : 28.6.2009, 23:37:41
    Author     : lucas
--%>

<%@include file="/WEB-INF/jspf/logged-in.jspf" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><f:message key="manage-users" /></title>
    </head>
    <body>
        <h1><f:message key="manage-users" /></h1>
        <div>
            <table>
                <thead>
                    <th><f:message key="login" /></th>
                    <th><f:message key="name" /></th>
                    <th><f:message key="role" /></th>
                    <th><f:message key="actions" /></th>
                </thead>
                <tbody>
                    <security:authentication property="principal.username" var="loggedUserId" />
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td><c:out value="${user.id}" /></td>
                            <td><c:out value="${user.name}" /></td>
                            <td><c:out value="${user.role}" /></td>
                            <td>
                                <a href="<c:out value="edit-${user.id}.do" />"><f:message key="edit" /></a>
                                <c:if test="${!(loggedUserId eq user.id)}">
                                 | <a href="<c:out value="delete-${user.id}.do" />"><f:message key="delete" /></a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
