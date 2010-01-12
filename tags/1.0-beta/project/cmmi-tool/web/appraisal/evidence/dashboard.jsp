<%-- 
    Document   : evidence repository
    Created on : 27.9.2009, 13:31:11
    Author     : Lukas Strmiska
--%>

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="evidence-repository" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><f:message key="evidence-repository" /></h1>
            <ul>
                <li><a href="add.do"><f:message key="add-evidence" /></a></li>
                <c:if test="${auditor}">
                    <li><a href="characterize.do"><f:message key="characterize-evidence" /></a></li>
                </c:if>
            </ul>
            <table class="tablesorter">
                <thead>
                    <tr>
                        <th><f:message key="status" /></th>
                        <th><f:message key="mapped" /></th>
                        <th><f:message key="name" /></th>
                        <th><f:message key="document-link" /></th>
                        <th><f:message key="user" /></th>
                        <th><f:message key="actions" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${evidence}" var="ev">
                        <tr>
                            <td><c:out value="${ev.status}" /></td>
                            <td><c:if test="${!(empty ev.mappings)}"><f:message key="yes" /></c:if></td>
                            <td><c:out value="${ev.name}" /></td>
                            <td><a href="<c:url value="${ev.link}" />"><c:out value="${ev.link}" /></a></td>
                            <td><c:out value="${ev.modifiedBy.name}" /></td>

                            <td>
                                <a href="<strmik:URLEncode value="edit-${ev.id}.do" />"><f:message key="edit" /></a>
                                | <a class="confirmable" href="<strmik:URLEncode value="delete-${ev.id}.do"  />">
                                <f:message key="delete" /></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
