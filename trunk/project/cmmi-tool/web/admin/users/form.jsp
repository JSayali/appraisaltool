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

                        <div class="ctrlHolder <spring:bind path="user.id"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="id" cssClass="errorField" element="p" />
                            <label for="id" ><f:message key="login" /></label>
                            <form:input disabled="${!create}" path="id" id="id" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="user.password"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="password" cssClass="errorField" element="p" />
                            <label for="password" ><f:message key="password" /></label>
                            <form:password path="password" id="password" cssClass="textInput" maxlength="25" size="35"  />
                        </div>

                        <div class="ctrlHolder <spring:bind path="user.password2"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="password2" cssClass="errorField" element="p" />
                            <label for="password2" ><f:message key="retype-password" /></label>
                            <form:password path="password2" id="password2" cssClass="textInput" maxlength="25" size="35"  />
                        </div>

                        <div class="ctrlHolder <spring:bind path="user.name"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="name" cssClass="errorField" element="p" />
                            <label for="name" ><f:message key="name" /></label>
                            <form:input path="name" id="name" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="user.email"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="email" cssClass="errorField" element="p" />
                            <label for="email" ><f:message key="email" /></label>
                            <form:input path="email" id="email" cssClass="textInput" maxlength="25" size="35" />
                        </div>

                        <div class="ctrlHolder <spring:bind path="user.role"><c:if test="${status.error}">error</c:if></spring:bind>">
                            <form:errors path="role" cssClass="errorField" element="p" />
                            <label for="role" ><f:message key="role" /></label>
                            <f:message key="please-select" var="msg" />
                            <form:select cssClass="selectInput"  path="role">
                                <form:option value="-" label="-- ${msg}"/>
                                <form:options items="${roles}" />
                            </form:select>
                        </div>

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
