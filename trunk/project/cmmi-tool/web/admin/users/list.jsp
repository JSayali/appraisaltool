<%-- 
    Document   : User management
    Created on : 28.6.2009, 23:37:41
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="page-users" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div>
            <h1><f:message key="page-users" /></h1>
            <div>
                <ul>
                    <li><a href="add.do"><f:message key="add-user" /></a></li>
                </ul>
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
                                <td><f:message key="${user.roleLowerCase}" /></td>
                                <td>
                                    <a href="<c:out value="edit-${user.id}.do" />"><f:message key="edit" /></a>
                                    <c:if test="${!(loggedUserId eq user.id)}">
                                        | <a class="confirmable" href="<c:out value="delete-${user.id}.do" />"><f:message key="delete" /></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
