<%-- 
    Document   : user edit form
    Created on : 22.6.2009, 22:08:22
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <c:set var="create" value="${user.newUser}" />
        <title>
            <c:if test="${create}">
                <f:message key="add-user" />
            </c:if>
            <c:if test="${!create}">
                <f:message key="edit-user" />
            </c:if>
        </title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1>
                <c:if test="${create}">
                    <f:message key="add-user" />
                </c:if>
                <c:if test="${!create}">
                    <f:message key="edit-user" />
                </c:if>
            </h1>
            <div style="width:600px">
                <form:form commandName="user" cssClass="uniForm">
                    <spring:hasBindErrors htmlEscape="false" name="user">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">

                        <legend><f:message key="user-details" /></legend>

                        <strmik:inputText object="user" property="id" title="login" disabled="${!create}" />
                        <strmik:inputText object="user" property="password" password="${true}" />
                        <strmik:inputText object="user" property="password2" title="retype-password" password="${true}" />
                        <strmik:inputText object="user" property="name" />
                        <strmik:inputText object="user" property="email" />
                        <strmik:options object="user" property="role" itemMap="${roles}" />

                        <div class="ctrlHolder <spring:bind path="user.enabled"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <p class="label"><f:message key="options" /></p>
                            <div class="multiField">
                                <form:errors path="enabled" cssClass="errorField" element="p" />
                                <label class="inlineLabel" for="nonexpired">
                                    <form:checkbox id="nonexpired" path="accountNonExpired"/>
                                    <f:message key="non-expired" />
                                </label>
                                <label class="inlineLabel" for="nonlocked">
                                    <form:checkbox id="nonlocked" path="accountNonLocked"/>
                                    <f:message key="non-locked" />
                                </label>
                                <label class="inlineLabel" for="enabled">
                                    <form:checkbox id="enabled" path="enabled"/>
                                    <f:message key="enabled" />
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
