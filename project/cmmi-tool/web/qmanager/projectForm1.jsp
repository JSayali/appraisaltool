<%-- 
    Document   : projectForm1 - Manage project
    Created on : 6.7.2009, 22:21:43
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title>
            <c:if test="${project.newProject}">
                <f:message key="add-project" />
            </c:if>
            <c:if test="${!project.newProject}">
                <f:message key="edit-project" />
            </c:if>
        </title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1>
                <c:if test="${project.newProject}">
                    <f:message key="add-project" />
                </c:if>
                <c:if test="${!project.newProject}">
                    <f:message key="edit-project" />
                </c:if>
                (${project.organization.name})
            </h1>
            <div style="width:600px">
                <form:form commandName="project" cssClass="uniForm" action="save-project.do">
                    <spring:hasBindErrors htmlEscape="false" name="org">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">

                        <legend><f:message key="project-details" /></legend>

                        <strmik:inputText object="project" property="id" title="acronym" disabled="${!project.newProject}" />
                        <strmik:inputText object="project" property="name" />
                        <strmik:options object="project" property="model" items="${models}" itemLabel="name" />
                        <strmik:options object="project" property="method" items="${methods}" itemLabel="name" />

                    </fieldset>

                    <div class="buttonHolder">
                        <button class="resetButton" type="reset"><f:message key="reset" /></button>
                        <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>
