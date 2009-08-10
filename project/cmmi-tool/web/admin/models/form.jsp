<%-- 
    Document   : form - Manage project
    Created on : 23.7.2009, 22:55:11
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <c:if test="${model.new}">
            <f:message key="add-model" var="title" />
        </c:if>
        <c:if test="${!model.new}">
            <f:message key="edit-model" var="title" />
        </c:if>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div style="width:600px">
                <form:form commandName="model" cssClass="uniForm" action="save-model.do">
                    <spring:hasBindErrors htmlEscape="false" name="model">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">

                        <legend><f:message key="model-details" /></legend>

                        <strmik:inputText object="model" property="acronym" />
                        <strmik:inputText object="model" property="name" />

                        <strmik:options object="model" property="highestML" items="${levels0}" title="highest-ml" />

                        <strmik:inputTextArea object="model" property="description" />

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
