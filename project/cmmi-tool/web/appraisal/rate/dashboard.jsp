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
                        <form class="uniForm" action="../" method="get">
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
                                        <f:message key="rate-goal">
                                            <f:param value="${node.goal.name}" />
                                        </f:message>
                                    </c:if>
                                    <c:if test="${ratePractice}">
                                        <f:message key="rate-practice">
                                            <f:param value="${node.practice.name}" />
                                        </f:message>
                                    </c:if>
                                </legend>
                                <c:if test="${!empty saved}">
                                    <div id="OKMsg">
                                        <p><f:message key="saved" /></p>
                                    </div>
                                </c:if>

                                <c:if test="${rateOrg}">
                                    <strmik:options pleaseSelect="false" object="node" property="maturityRating" items="${levels0}" title="ou-maturity-rating" disabled="${!rateOrgEnabled}" />
                                </c:if>
                                <c:if test="${ratePA}">
                                    <strmik:options pleaseSelect="false" object="node" property="processAreaCapRatingScale" items="${node.processAreaCapScales}" title="process-area-cap-rating" itemLabel="name" disabled="${empty node.processAreaCapRating}" />
                                    <strmik:options pleaseSelect="false" object="node" property="processAreaSatRatingScale" items="${node.processAreaSatisfactionScales}" itemLabel="name" title="process-area-satisfaction-rating" disabled="${empty node.processAreaSatisfactionRating}" />
                                </c:if>
                                <c:if test="${rateGoal}">
                                    <f:message key="aggregated-satisfaction" var="aggregatedHint">
                                        <f:param value="${aggregatedMessage}" />
                                    </f:message>
                                    <strmik:options pleaseSelect="false" object="node" itemLabel="name" property="rating" items="${scales}" title="goal-satisfaction-rating" disabled="${!rateGoalEnabled}" hint="${aggregatedHint}" />
                                </c:if>
                                <c:if test="${ratePractice}">
                                    <f:message key="practice-aggregation" var="aggregatedHint">
                                        <f:param value="${aggregatedMessage}" />
                                    </f:message>
                                    <strmik:options pleaseSelect="false" object="node" itemLabel="name" property="rating" items="${scales}" title="practice-implementation-char" disabled="${!ratePracticeEnabled}" hint="${aggregatedHint}" />

                                    <strmik:inputTextArea object="node" property="oppurtunities" cols="40" rows="10" />
                                    <strmik:inputTextArea object="node" property="presenceAbsence" cols="40" rows="10" />
                                    <strmik:inputTextArea object="node" property="notes" cols="40" rows="10" />

                                </c:if>
                                <c:if test="${!rateOrg && recordFinding}">
                                    <strmik:inputTextArea object="node" property="strength" cols="40" rows="10" />
                                    <strmik:inputTextArea object="node" property="weakness" cols="40" rows="10" />
                                </c:if>
                            </fieldset>
                            <div class="buttonHolder">
                                <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                            </div>
                        </form:form>

                        <hr/>
                        <div>
                            <c:if test="${rateOrg}">
                                <h3><f:message key="assesed-process-areas" /></h3>

                                <table class="tablesorter">
                                    <thead>
                                    <th><f:message key="satisfaction" /></th>
                                    <th><f:message key="name" /></th>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="pa" items="${pas}">
                                            <tr>
                                                <td><c:out value="${pa.rating.name}" /></td>
                                                <td><c:out value="${pa.processArea.name}" /></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                            </c:if>
                            <c:if test="${ratePA}">
                                <h3><f:message key="related-goals" /></h3>

                                <table class="tablesorter">
                                    <thead>
                                    <th><f:message key="satisfaction" /></th>
                                    <th><f:message key="name" /></th>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="go" items="${goals}">
                                            <tr>
                                                <td><c:out value="${go.rating.name}" /></td>
                                                <td><c:out value="${go.goal.name}" /></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                            </c:if>
                            <c:if test="${rateGoal}">
                                <h3><f:message key="related-practices" /></h3>
                                <table class="tablesorter">
                                    <thead>
                                    <th><f:message key="satisfaction" /></th>
                                    <th><f:message key="name" /></th>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="pr" items="${practices}">
                                            <tr>
                                                <td><c:out value="${pr.rating.name}" /></td>
                                                <td><c:out value="${pr.practice.name}" /></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                            <c:if test="${ratePractice}">
                                <h3><f:message key="related-evidence" /></h3>

                                <c:forEach items="${evidenceMapping}" var="em">
                                    <c:set var="inst" value="${em.key}" />
                                    <c:set var="rating" value="${evidenceRatings[inst.id]}" />
                                    <c:set var="evidence" value="${em.value}" />
                                    <h4><c:out value="${inst.name}" /></h4>
                                    <table border="1" style="border-collapse: collapse;">
                                        <thead>
                                            <tr>
                                                <th><f:message key="characterization" /></th>
                                                <th><f:message key="adequacy" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td><c:out value="${rating.characterizePracticeImplementation.name}" /></td>
                                                <td><f:message key="PracticeEvidenceAdequacy.${rating.evidenceAdequacy}" /></td>
                                            </tr>
                                        </tbody>
                                    </table>

                                    <table class="tablesorter">
                                        <thead>
                                        <th><f:message key="name" /></th>
                                        <th><f:message key="characterization" /></th>
                                        <th><f:message key="indicator-type" /></th>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ev" items="${evidence}">
                                                <tr>
                                                    <td><c:out value="${ev.evidence.name}" /></td>
                                                    <td><f:message key="EvidenceCharacteristic.${ev.characteristic}" /></td>
                                                    <td><f:message key="IndicatorType.${ev.indicatorType}" /></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                        
                                </c:forEach>



                            </c:if>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
