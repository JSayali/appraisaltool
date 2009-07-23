<%-- 
    Document   : Model management
    Created on : 23.7.2009, 22:00:14
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="page-models" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div>
            <h1><f:message key="page-models" /></h1>
            <div>
                <ul>
                    <li><a href="add.do"><f:message key="add-model" /></a></li>
                </ul>
                <table class="tablesorter">
                    <thead>
                        <tr>
                            <th><f:message key="acronym" /></th>
                            <th><f:message key="name" /></th>
                            <th><f:message key="highest-ml" /></th>
                            <th><f:message key="actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${models}" var="model">
                            <tr>
                                <td><c:out value="${model.id}" /></td>
                                <td><c:out value="${model.name}" /></td>
                                <td><c:out value="${model.highestML}" /></td>
                                <td>
                                    <a href="<c:out value="edit-${model.id}.do" />"><f:message key="edit" /></a>
                                    | <a class="confirmable" href="<c:out value="delete-${model.id}.do" />"><f:message key="delete" /></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
