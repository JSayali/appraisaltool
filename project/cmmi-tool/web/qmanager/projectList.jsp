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
            <div style="width:600px">
                <form class="uniForm" action="" method="post">
                    <fieldset class="blockLabels">
                        <legend><f:message key="choose-organization-first" /></legend>

                        <div class="ctrlHolder">
                            <label for=orgId">
                                <f:message key="organization" />
                            </label>

                            <select name="orgId">
                                <option value="">-- <f:message key="please-select" /></option>
                                <c:forEach items="${organizations}" var="org">
                                    <c:if test="${organization.id eq org.key}">
                                        <option selected="selected" value="${org.key}">${org.value}</option>
                                    </c:if>
                                    <c:if test="${!(organization.id eq org.key)}">
                                        <option value="${org.key}">${org.value}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                        </div>
                    </fieldset>
                </form>
            </div>
            <div>
                <c:if test="${!(empty organization.id)}">
                    <h3><f:message key="organization" />: <c:out value="${organization.name}"/></h3>
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
                            <c:forEach items="${organization.projects}" var="project">
                                <tr>
                                    <td><c:out value="${project.id}" /></td>
                                    <td><c:out value="${project.name}" /></td>
                                    <td>
                                        <ul>
                                            <c:forEach items="${project.team}" var="member">
                                                <li><c:out value="${member.user.name}" /> (<f:message key="team-role-${member.teamRoleLowerCase}" />)</li>
                                            </c:forEach>
                                        </ul>
                                    </td>
                                    <td>
                                        <a href="<strmik:URLEncode value="edit-${project.id}.do" />"><f:message key="edit" /></a>
                                        | <a class="confirmable" href="<strmik:URLEncode value="delete-${project.id}.do"  />"><f:message key="delete" /></a>
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
