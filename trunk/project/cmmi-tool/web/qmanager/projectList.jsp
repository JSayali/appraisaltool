<%-- 
    Document   : quality manager dashboard
    Created on : 6.7.2009, 18:01:08
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="page-qmanager" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><f:message key="page-qmanager" /></h1>
            <h2><f:message key="manage-appraisal-projects" /></h2>
            <form:form commandName="wrapper" cssClass="uniForm">
                <fieldset class="blockLabels">
                    <legend><f:message key="choose-organization-first" /></legend>
                    <strmik:options object="wrapper" property="selectedOrganizationId" title="active-organizations" items="${wrapper.aviableOrganizations}" />
                    <div class="buttonHolder">
                        <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                    </div>
                </fieldset>
            </form:form>
            <div>
                <c:if test="${!(empty wrapper.selectedOrganizationId)}">
                    <h3><f:message key="organization" />: <c:out value="${wrapper.selectedOrganizationName}"/></h3>
                    <ul>
                        <li><a href="add.do"><f:message key="add-project" /></a></li>
                    </ul>
                    <table class="tablesorter">
                        <thead>
                            <tr>
                                <th><f:message key="acronym" /></th>
                                <th><f:message key="name" /></th>
                                <th><f:message key="team" /></th>
                                <th><f:message key="actions" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${wrapper.projects}" var="project">
                                <tr>
                                    <td><c:out value="${project.id}" /></td>
                                    <td><c:out value="${project.name}" /></td>
                                    <td>
                                        <ul>
                                            <c:forEach items="${project.team}" var="member">
                                                <li><c:out value="${member.user.name}" /> (<f:message key="team-role-${member.teamRole}" />)</li>
                                            </c:forEach>
                                        </ul>
                                    </td>
                                    <td>
                                        <a href="<c:out value="edit-${project.id}.do" />"><f:message key="edit" /></a>
                                        | <a class="confirmable" href="<c:out value="delete-${project.id}.do" />"><f:message key="delete" /></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>
        </div>
    </body>
</html>
