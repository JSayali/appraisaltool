<%-- 
    Document   : form - Manage evidence
    Created on : 27.9.2009, 14:44:51
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <c:if test="${evidence.new}">
            <f:message key="add-evidence" var="title" />
        </c:if>
        <c:if test="${!evidence.new}">
            <f:message key="edit-evidence" var="title" />
        </c:if>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div style="width:600px">
                <form:form commandName="evidence" cssClass="uniForm" action="save-evidence.do">
                    <spring:hasBindErrors htmlEscape="false" name="method">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">
                        <legend><f:message key="evidence-details" /></legend>

                        <strmik:inputText object="evidence" property="name" />
                        <strmik:inputText object="evidence" property="source" />
                        <strmik:inputText object="evidence" property="link" />
                        <strmik:inputText object="evidence" property="label" />

                        <strmik:options object="evidence" property="type" items="${types}" title="type" />
                        <strmik:options object="evidence" property="status" items="${statuses}" title="status" />

                        <strmik:inputTextArea object="evidence" property="description" />
                    </fieldset>

                    <div class="buttonHolder">
                        <button class="resetButton" type="reset"><f:message key="reset" /></button>
                        <button class="primaryAction" type="submit"><f:message key="next-step" /></button>
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>
