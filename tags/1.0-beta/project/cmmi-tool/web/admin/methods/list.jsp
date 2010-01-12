<%-- 
    Document   : Method management
    Created on : 11.8.2009, 21:02:43
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <f:message key="page-methods" var="title" />
        <title>${title}</title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div>
            <h1>${title}</h1>
            <div>
                <ul>
                    <li><a href="add.do"><f:message key="add-method" /></a></li>
                </ul>
                <table class="tablesorter">
                    <thead>
                        <tr>
                            <th><f:message key="name" /></th>
                            <th><f:message key="description" /></th>
                            <th><f:message key="actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${methods}" var="method">
                            <tr>
                                <td><c:out value="${method.name}" /></td>
                                <td><c:out value="${method.description}" /></td>
                                <td>
                                    <a href="<c:out value="edit-${method.id}.do" />"><f:message key="edit" /></a>
                                    | <a class="confirmable" href="<c:out value="delete-${method.id}.do" />"><f:message key="delete" /></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>