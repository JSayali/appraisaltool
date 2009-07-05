<%-- 
    Document   : Organization management
    Created on : 5.7.2009, 21:55:41
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
            <h1><f:message key="page-organizations" /></h1>
            <div>
                <ul>
                    <li><a href="add.do"><f:message key="add-organization" /></a></li>
                </ul>
                <table class="tablesorter">
                    <thead>
                        <tr>
                            <th><f:message key="name" /></th>
                            <th><f:message key="contactPerson" /></th>
                            <th><f:message key="email" /></th>
                            <th><f:message key="telephone" /></th>
                            <th><f:message key="actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${orgs}" var="org">
                            <tr>
                                <td><c:out value="${org.name}" /></td>
                                <td><c:out value="${org.contactPerson}" /></td>
                                <td><a href="mailto:${org.email}"><c:out value="${org.email}" /></a></td>
                                <td><c:out value="${org.telephone}" /></td>
                                <td>
                                    <a href="<c:out value="edit-${org.id}.do" />"><f:message key="edit" /></a>
                                     | <a class="confirmable" href="<c:out value="delete-${org.id}.do" />"><f:message key="delete" /></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
