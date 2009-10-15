<%-- 
    Document   : dashboard - Conduct appraisal rating dashboard
    Created on : 10.10.2009, 15:59:16
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <%@include file="/WEB-INF/jspf/treeview.jspf" %>
        <f:message key="page-rate" var="title"  />
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div>
                <div id="treecontrol" style="display: block;">
                    <a href="#" title="<f:message key="collapse-all-help" />">
                        <img src="${pageContext.request.contextPath}/resources/img/treeview/minus.gif"/>
                        <f:message key="collapse-all" />
                    </a>
                    <a href="#" title="<f:message key="expand-all-help" />">
                        <img src="${pageContext.request.contextPath}/resources/img/treeview/plus.gif"/>
                        <f:message key="expand-all" />
                    </a>
                    <a href="#" title="<f:message key="toggle-all-help" />">
                        <f:message key="toggle-all" />
                    </a>
                </div>
                <div style="width:400px; float: left;">
                    <ul id="modeltree" class="filetree">
                        <strmik:tree node="${ratingTree}" color="${true}" />
                    </ul>
                    <div style="width:300px;">
                        <form class="uniForm" action="finish.do" method="get">
                            <div class="buttonHolder">
                                <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                            </div>
                        </form>
                    </div>
                </div>
                <div style="float: left;width:600px;">
                    <c:set var="displayForm" value="${rateOrg || ratePA || rateGoal || ratePractice}" />
                    <c:if test="${!displayForm}">
                        <f:message key="please-select-node" />
                    </c:if>
                    <c:if test="${displayForm}">
                        <form:form commandName="node" cssClass="uniForm" action="save-${node.class.simpleName}-${node.id}.do">
                            <spring:hasBindErrors htmlEscape="false" name="node">
                                <div id="errorMsg">
                                    <h3><f:message key="invalid-input" /></h3>
                                </div>
                            </spring:hasBindErrors>
                            <fieldset class="inlineLabels">

                                <legend>
                                    <c:if test="${rateOrg}">
                                        <f:message key="rate-org" />
                                    </c:if>
                                    <c:if test="${ratePA}">
                                        <f:message key="rate-pa" >
                                            <f:param value="${node.name}" />
                                        </f:message>
                                    </c:if>
                                    <c:if test="${rateGoal}">
                                        <f:message key="goal-details">
                                            <f:param value="${node.name}" />
                                        </f:message>
                                    </c:if>
                                    <c:if test="${ratePractice}">
                                        <f:message key="artifact-details">
                                            <f:param value="${node.name}" />
                                        </f:message>
                                    </c:if>
                                </legend>
                                <c:if test="${!empty saved}">
                                    <div id="OKMsg">
                                        <p><f:message key="saved" /></p>
                                    </div>
                                </c:if>


                                    <c:if test="${rateOrg}">
                                        <strmik:options pleaseSelect="false" object="node" property="maturityRating" items="${levels0}" title="ou-maturity-raitng" disabled="${!rateOrgEnabled}" />
                                    </c:if>
                                    <c:if test="${ratePA}">
                                        <strmik:options pleaseSelect="false" object="node" property="processAreaCapRatingScale" items="${node.processAreaCapScales}" title="process-area-cap-rating" itemLabel="name" disabled="${empty node.processAreaCapRating}" />
                                        <strmik:options pleaseSelect="false" object="node" property="processAreaSatRatingScale" items="${node.processAreaSatisfactionScales}" itemLabel="name" title="process-area-satisfaction-rating" disabled="${empty node.processAreaSatisfactionRating}" />

                                        <strmik:inputTextArea object="node" property="strength" title="pa-strength" cols="40" rows="10" />
                                        <strmik:inputTextArea object="node" property="weakness" title="pa-weaknes" cols="40" rows="10" />

                                    </c:if>
                                    <c:if test="${rateGoal}">

                                    </c:if>
                                    <c:if test="${ratePractice}">

                                    </c:if>



<%--
                                <strmik:inputText object="node" property="acronym" />
                                <strmik:inputText object="node" property="name" />
                                <c:if test="${ableAddGoal && !generic}">
                                    <strmik:options object="node" property="processGroup" items="${model.processGroups}" title="process-group" itemLabel="name" />
                                    <strmik:options object="node" property="maturityLevel" items="${levels1}" title="process-maturity" />
                                </c:if>
                                <c:if test="${ableAddArtifact}">
                                    <strmik:options object="node" property="practiceCapability" items="${levels0}" title="practice-capability" />
                                </c:if>

                                <c:if test="${!artifactEdit}">
                                    <strmik:inputTextArea object="node" property="summary" cols="60" />
                                    <c:if test="${!ableAddPractice}">
                                        <strmik:inputTextArea object="node" property="purpose" />
                                    </c:if>
                                </c:if>
                                <c:if test="${artifactEdit}">
                                    <div class="ctrlHolder">
                                        <label class="inlineLabel" for="direct">
                                            <form:checkbox id="direct" path="direct"/>
                                            <span><f:message key="direct-artifact" /></span>
                                        </label>
                                    </div>
                                </c:if>
--%>
                            </fieldset>

                            <div class="buttonHolder">
                                <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                            </div>

                        </form:form>

                        <hr/>
                        <div>
                        <c:if test="${rateOrg}">
                            <f:message key="assesed-process-areas" />
                            

                        </c:if>
                        <c:if test="${ratePA}">

                        </c:if>
                        <c:if test="${rateGoal}">

                        </c:if>
                        <c:if test="${ratePractice}">

                        </c:if>
                            </div>

                    </c:if>

                </div>
            </div>
        </div>

    </body>
</html>
