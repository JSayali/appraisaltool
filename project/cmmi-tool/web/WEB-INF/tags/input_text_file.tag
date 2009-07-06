<%-- 
    Document   : input_text_file
    Created on : 6.7.2009, 11:15:12
    Author     : Lukas Strmiska
--%>
<%@tag description="creates input text filed (spring form) with validation check and field label in uniform div. Must be inside spring:form tag." pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="f" uri="http://java.sun.com/jstl/fmt_rt" %>

<%@attribute name="property" required="true" %>
<%@attribute name="object" required="true" %>
<%@attribute name="disabled" required="false" description="default false" %>
<%@attribute name="title" required="false" description="title for value, if not se, its taken from property" %>
<%@attribute name="maxlength" required="false" description="default 25" %>
<%@attribute name="size" required="false" description="default 35"%>
<%@attribute name="password" required="false" description="default false" %>

<c:if test="${empty maxlength}">
    <c:set var="maxlength" value="25" />
</c:if>
<c:if test="${empty size}">
    <c:set var="size" value="35" />
</c:if>
<c:if test="${empty password}">
    <c:set var="password" value="${false}" />
</c:if>
<c:if test="${empty disabled}">
    <c:set var="disabled" value="false" />
</c:if>
<c:if test="${empty title}">
    <c:set var="title" value="${property}" />
</c:if>

<div class="ctrlHolder <spring:bind path="${object}.${property}"><c:if test="${status.error}">error</c:if></spring:bind>">
    <form:errors path="${property}" cssClass="errorField" element="p" />
    <label for="${property}" ><f:message key="${title}" /></label>
    <c:if test="${password}">
        <form:password path="${property}" id="${property}" cssClass="textInput" maxlength="${maxlength}" size="${size}" disabled="${disabled}" />
    </c:if>
    <c:if test="${!password}">
        <form:input path="${property}" id="${property}" cssClass="textInput" maxlength="${maxlength}" size="${size}" disabled="${disabled}" />
    </c:if>
</div>