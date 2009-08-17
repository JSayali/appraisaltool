<%-- 
    Document   : form - Manage method
    Created on : 11.8.2009, 21:34:15
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <c:if test="${method.new}">
            <f:message key="add-method" var="title" />
        </c:if>
        <c:if test="${!method.new}">
            <f:message key="edit-method" var="title" />
        </c:if>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div style="width:600px">
                <form:form commandName="method" cssClass="uniForm" action="save-method.do">
                    <spring:hasBindErrors htmlEscape="false" name="method">
                        <div id="errorMsg">
                            <h3><f:message key="invalid-input" /></h3>
                        </div>
                    </spring:hasBindErrors>
                    <fieldset class="inlineLabels">

                        <legend><f:message key="method-details" /></legend>

                        <strmik:inputText object="method" property="name" />
                        <strmik:inputText object="method" property="description" />

                        <div class="ctrlHolder">
                            <p class="label"><f:message key="record-finding-on" /></p>
                            <div class="multiField">
                                <label class="inlineLabel" for="findingOnTheOrgLevel">
                                    <form:checkbox id="findingOnTheOrgLevel" path="findingOnTheOrgLevel"/>
                                    <f:message key="org-level" />
                                </label>
                                <label class="inlineLabel" for="findingOnProcessArea">
                                    <form:checkbox id="findingOnProcessArea" path="findingOnProcessArea"/>
                                    <f:message key="process-area" />
                                </label>
                                <label class="inlineLabel" for="findingOnGoalLevel">
                                    <form:checkbox id="findingOnGoalLevel" path="findingOnGoalLevel"/>
                                    <f:message key="goal-level" />
                                </label>
                                <label class="inlineLabel" for="findingOnPracticeLevel">
                                    <form:checkbox id="findingOnPracticeLevel" path="findingOnPracticeLevel"/>
                                    <f:message key="practice-level" />
                                </label>
                            </div>
                        </div>
                        <div class="ctrlHolder">
                            <p class="label"><f:message key="rate" /></p>
                            <div class="multiField">
                                <label class="inlineLabel" for="rateProcessAreaCapLevel">
                                    <form:checkbox id="rateProcessAreaCapLevel" path="rateProcessAreaCapLevel"/>
                                    <f:message key="PROCESS_AREA_CAP_LEVEL" />
                                </label>
                                <label class="inlineLabel" for="rateProcessAreaSatisfaction">
                                    <form:checkbox id="rateProcessAreaSatisfaction" path="rateProcessAreaSatisfaction"/>
                                    <f:message key="PROCESS_AREA_SATISFACTION" />
                                </label>
                                <label class="inlineLabel" for="rateGoalSatisfaction">
                                    <form:checkbox id="rateGoalSatisfaction" path="rateGoalSatisfaction"/>
                                    <f:message key="GOAL_SATISFACTION" />
                                </label>
                                <label class="inlineLabel" for="rateOrgMaturityLevel">
                                    <form:checkbox id="rateOrgMaturityLevel" path="rateOrgMaturityLevel"/>
                                    <f:message key="ORG_MATURITY_LEVEL" />
                                </label>
                                <label class="inlineLabel" for="charPracticeImplementation">
                                    <form:checkbox id="charPracticeImplementation" path="charPracticeImplementation"/>
                                    <f:message key="CHAR_PRACTICE_IMPL" />
                                </label>
                            </div>
                        </div>

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
