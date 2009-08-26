<%--
    Document   : Organization edit form
    Created on : 5.7.2009, 21:55:52
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title>
            <c:if test="${create}">
                <f:message key="add-organization" />
            </c:if>
            <c:if test="${!create}">
                <f:message key="edit-organization" />
            </c:if>
        </title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1>
                <c:if test="${create}">
                    <f:message key="add-organization" />
                </c:if>
                <c:if test="${!create}">
                    <f:message key="edit-organization" />
                </c:if>
            </h1>
            <div style="width:600px">
                <form:form commandName="org" cssClass="uniForm">
                    <spring:hasBindErrors htmlEscape="false" name="org">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">

                        <legend><f:message key="organization-details" /></legend>

                        <strmik:inputText object="org" property="name" />
                        <strmik:inputText object="org" property="address" />
                        <strmik:inputText object="org" property="contactPerson" />
                        <strmik:inputText object="org" property="email" />
                        <strmik:inputText object="org" property="telephone" />
                        <strmik:inputText object="org" property="fax" />

                        <div class="ctrlHolder <spring:bind path="org.active"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <p class="label"><f:message key="options" /></p>
                            <div class="multiField">
                                <form:errors path="active" cssClass="errorField" element="p" />
                                <label class="inlineLabel" for="active">
                                    <form:checkbox id="active" path="active"/>
                                    <f:message key="active" />
                                </label>
                            </div>
                        </div>

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
