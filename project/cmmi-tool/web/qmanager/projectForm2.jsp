<%-- 
    Document   : projectForm2 - Manage project team
    Created on : 9.7.2009, 18:36:13
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title>
            <f:message key="manage-project-team"  >
                <f:param value="${project.name}" />
            </f:message>
        </title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1>
                <f:message key="manage-project-team"  >
                    <f:param value="${project.name}" />
                </f:message>
            </h1>
            <div style="width:600px">
                <form:form commandName="teamMember" cssClass="uniForm" action="add-member.do">
                    <fieldset class="inlineLabels">
                        <legend><f:message key="add-new-member" /></legend>
                        <c:if test="${!empty saved}">
                            <div id="OKMsg">
                                <p><f:message key="team-saved" /></p>
                            </div>
                        </c:if>
                        <strmik:options object="teamMember" property="user" items="${users}" title="user" itemLabel="name" />
                        <strmik:options object="teamMember" property="teamRole" itemMap="${teamRoles}" title="team-role" />

                        <div class="buttonHolder">
                            <button class="resetButton" type="reset"><f:message key="reset" /></button>
                            <button class="primaryAction" type="submit"><f:message key="add-new-member" /></button>
                        </div>
                    </fieldset>
                </form:form>
                <h4><f:message key="current-team" /></h4>
                <table>
                    <thead>
                        <tr>
                            <td><f:message key="member" /></td><td><f:message key="actions" /></td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${project.team}" var="member">
                            <tr>
                                <td><c:out value="${member.user.name}"/> (<f:message key="team-role-${member.teamRoleLowerCase}" />)</td>
                                <td><a class="confirmable" href="<c:out value="remove-member-${member.id}.do" />"><f:message key="delete" /></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <form method="get" action="finish-team.do" class="uniForm">
                    <fieldset class="inlineLabels">
                        <legend />
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="finish" /></button>
                        </div>
                    </fieldset>
                </form>
            </div>

        </div>
    </body>
</html>
