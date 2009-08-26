<%-- 
    Document   : options_file
    Created on : 6.7.2009, 18:06:35
    Author     : lucas
--%>

<%@tag description="creates options filed (spring form) with validation check and field label in uniform div. Must be inside spring:form tag." pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="f" uri="http://java.sun.com/jstl/fmt_rt" %>

<%@attribute name="property" required="true" %>
<%@attribute name="object" required="true" %>
<%@attribute name="items" required="false" type="java.util.Collection" %>
<%@attribute name="itemMap" required="false" type="java.util.Map" %>
<%@attribute name="itemLabel" required="false" %>
<%@attribute name="title" required="false" description="title for value, if not se, its taken from property" %>
<%@attribute name="pleaseSelect" required="false" description="default true" %>
<%@attribute name="pleaseMessage" required="false" description="default is taken from please-select" %>

<c:if test="${empty title}">
    <c:set var="title" value="${property}" />
</c:if>
<c:if test="${empty pleaseSelect}">
    <c:set var="pleaseSelect" value="${true}" />
</c:if>
<c:if test="${empty pleaseSelectMessage}">
    <f:message key="please-select" var="pleaseSelectMessage" />
</c:if>

<div class="ctrlHolder <spring:bind path="${object}.${property}"><c:if test="${status.error}">error</c:if></spring:bind>">
    <form:errors path="${property}" cssClass="errorField" element="p" />
    <label for="${property}" ><f:message key="${title}" /></label>
    <form:select cssClass="selectInput"  path="${property}">
        <c:if test="${pleaseSelect}">
            <form:option value="" label="-- ${pleaseSelectMessage}"/>
        </c:if>
        <c:if test="${empty itemMap}">
            <c:if test="${empty itemLabel}">
                <form:options items="${items}"/>
            </c:if>
            <c:if test="${!empty itemLabel}">
                <form:options items="${items}" itemLabel="${itemLabel}" />
            </c:if>
        </c:if>
        <c:if test="${empty items}">
            <c:if test="${empty itemLabel}">
                <form:options items="${itemMap}"/>
            </c:if>
            <c:if test="${!empty itemLabel}">
                <form:options items="${itemMap}" itemLabel="${itemLabel}" />
            </c:if>
        </c:if>
    </form:select>
</div>