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

                        <div class="ctrlHolder <spring:bind path="org.name"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="name" cssClass="errorField" element="p" />
                            <label for="name" ><f:message key="name" /></label>
                            <form:input path="name" id="name" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="org.address"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="address" cssClass="errorField" element="p" />
                            <label for="address" ><f:message key="address" /></label>
                            <form:input path="address" id="address" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="org.contactPerson"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="contactPerson" cssClass="errorField" element="p" />
                            <label for="contactPerson" ><f:message key="contactPerson" /></label>
                            <form:input path="contactPerson" id="contactPerson" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="org.email"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="email" cssClass="errorField" element="p" />
                            <label for="email" ><f:message key="email" /></label>
                            <form:input path="email" id="email" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="org.telephone"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="telephone" cssClass="errorField" element="p" />
                            <label for="telephone" ><f:message key="telephone" /></label>
                            <form:input path="telephone" id="telephone" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="org.fax"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="fax" cssClass="errorField" element="p" />
                            <label for="fax" ><f:message key="fax" /></label>
                            <form:input path="fax" id="fax" cssClass="textInput" maxlength="25" size="35" />
                        </div>

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
