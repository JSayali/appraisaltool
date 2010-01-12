<%-- 
    Document   : aggregation - Define aggregation rules
    Created on : 19.8.2009, 17:38:21
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <style type="text/css">
          @import "${pageContext.request.contextPath}/resources/css/aggregation.css";
        </style>
        <f:message key="define-aggregation-rules" var="title" >
            <f:param value="${method.name}" />
        </f:message>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div>
                <c:if test="${method.charPracticeImplementation}">
                    <div>
                        <h2><f:message key="practice-rule" /></h2>
                        <c:if test="${empty practiceRule}">
                            <h3><f:message key="add-rule" /></h3>
                            <form action="addpracticerule.do" method="post">
                        </c:if>
                        <c:if test="${!(empty practiceRule)}">
                            <h3><f:message key="edit-rule" ><f:param value="${practiceRule.ruleNo}" /></f:message></h3>
                            <form action="editpracticerule-${practiceRule.id}.do" method="post">
                        </c:if>
                            <table class="rulesTable">
                                <thead>
                                <tr>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                        <th>-&gt;</th>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <td>
                                            <select name="${scale.id}-source">
                                                <option value="NO" <c:if test="${!(empty practiceRule) && strmik:isRuleCompletion(scale,practiceRule,'NO',true)}">selected="selected"</c:if>> <f:message key="rule-NO" /> </option>
                                                <option value="YES" <c:if test="${!(empty practiceRule) && strmik:isRuleCompletion(scale,practiceRule,'YES',true)}">selected="selected"</c:if>> <f:message key="rule-YES" /> </option>
                                                <option value="YES_NO" <c:if test="${!(empty practiceRule) && strmik:isRuleCompletion(scale,practiceRule,'YES_NO',true)}">selected="selected"</c:if>> <f:message key="rule-YES_NO" /> </option>
                                            </select>
                                        </td>
                                    </c:forEach>
                                        <td>-&gt;</td>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <td>
                                            <select name="${scale.id}-target">
                                                <option value="NO" <c:if test="${!(empty practiceRule) && strmik:isRuleCompletion(scale,practiceRule,'NO',false)}">selected="selected"</c:if>> <f:message key="rule-NO" /> </option>
                                                <option value="YES" <c:if test="${!(empty practiceRule) && strmik:isRuleCompletion(scale,practiceRule,'YES',false)}">selected="selected"</c:if>> <f:message key="rule-YES" /> </option>
                                            </select>
                                        </td>
                                    </c:forEach>
                                    </tr>
                                </tbody>
                            </table>
                            <button type="submit"><strong><f:message key="submit" /></strong></button>
                        </form>
                        <h3><f:message key="existing-rules" /></h3>
                        <table class="rulesTable">
                            <thead>
                                <tr>
                                    <th><f:message key="id" /></th>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                    <th>-&gt;</th>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                    <th><f:message key="actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${method.sortedPracticeRuleAggregation}" var="rule">
                                    <tr>
                                        <td><c:out value="${rule.ruleNo}" /></td>
                                        <c:forEach items="${method.practiceImplementation}" var="scale">
                                            <td>
                                                <c:set var="found" value="${false}" />
                                                <c:forEach items="${rule.sources}" var="aRule">
                                                    <c:if test="${aRule.scale eq scale}">
                                                        <f:message key="rule-${aRule.ruleCompletion}" />
                                                        <c:set var="found" value="${true}" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${!found}">
                                                    <f:message key="rule-NO" />
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                        <td>-&gt;</td>
                                        <c:forEach items="${method.practiceImplementation}" var="scale">
                                            <td>
                                                <c:set var="found" value="${false}" />
                                                <c:forEach items="${rule.targets}" var="aRule">
                                                    <c:if test="${aRule.scale eq scale}">
                                                        <f:message key="rule-${aRule.ruleCompletion}" />
                                                        <c:set var="found" value="${true}" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${!found}">
                                                    <f:message key="rule-NO" />
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                        <td>
                                            <c:choose>
                                                <c:when test="${!(empty practiceRule) && (rule eq practiceRule)}">
                                                    <f:message key="na-being-edited" />
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:out value="editpracticerule-${rule.id}.do" />"><f:message key="edit" /></a>
                                                    | <a class="confirmable" href="<c:out value="deleterule-${rule.id}.do" />"><f:message key="delete" /></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </c:if>
                <c:if test="${method.rateGoalSatisfaction}">
                    <div>
                        <h2><f:message key="goal-rule" /></h2>
                        <c:if test="${!method.charPracticeImplementation}">
                            <f:message key="no-practice-rule" />
                        </c:if>
                        <c:if test="${method.charPracticeImplementation}">
                            <c:if test="${empty goalRule}">
                                <h3><f:message key="add-rule" /></h3>
                                <form action="addgoalrule.do" method="post">
                            </c:if>
                            <c:if test="${!(empty goalRule)}">
                                <h3><f:message key="edit-rule" ><f:param value="${goalRule.ruleNo}" /></f:message></h3>
                                <form action="editgoalrule-${goalRule.id}.do" method="post">
                            </c:if>
                            <table class="rulesTable">
                                <thead>
                                <tr>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                        <th>-&gt;</th>
                                    <c:forEach items="${method.goalSatisfaction}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <td>
                                            <select name="${scale.id}-source">
                                                <option value="NO" <c:if test="${!(empty goalRule) && strmik:isRuleCompletion(scale,goalRule,'NO',true)}">selected="selected"</c:if>> <f:message key="rule-NO" /> </option>
                                                <option value="YES" <c:if test="${!(empty goalRule) && strmik:isRuleCompletion(scale,goalRule,'YES',true)}">selected="selected"</c:if>> <f:message key="rule-YES" /> </option>
                                                <option value="YES_NO" <c:if test="${!(empty goalRule) && strmik:isRuleCompletion(scale,goalRule,'YES_NO',true)}">selected="selected"</c:if>> <f:message key="rule-YES_NO" /> </option>
                                            </select>
                                        </td>
                                    </c:forEach>
                                        <td>-&gt;</td>
                                    <c:forEach items="${method.goalSatisfaction}" var="scale">
                                        <td>
                                            <select name="${scale.id}-target">
                                                <option value="NO" <c:if test="${!(empty goalRule) && strmik:isRuleCompletion(scale,goalRule,'NO',false)}">selected="selected"</c:if>> <f:message key="rule-NO" /> </option>
                                                <option value="YES" <c:if test="${!(empty goalRule) && strmik:isRuleCompletion(scale,goalRule,'YES',false)}">selected="selected"</c:if>> <f:message key="rule-YES" /> </option>
                                            </select>
                                        </td>
                                    </c:forEach>
                                    </tr>
                                </tbody>
                            </table>
                            <button type="submit"><strong><f:message key="submit" /></strong></button>
                        </form>
                        <h3><f:message key="existing-rules" /></h3>
                        <table class="rulesTable">
                            <thead>
                                <tr>
                                    <th><f:message key="id" /></th>
                                    <c:forEach items="${method.practiceImplementation}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                    <th>-&gt;</th>
                                    <c:forEach items="${method.goalSatisfaction}" var="scale">
                                        <th>${scale.scaleString}</th>
                                    </c:forEach>
                                    <th><f:message key="actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${method.sortedGoalRuleAggregation}" var="rule">
                                    <tr>
                                        <td><c:out value="${rule.ruleNo}" /></td>
                                        <c:forEach items="${method.practiceImplementation}" var="scale">
                                            <td>
                                                <c:set var="found" value="${false}" />
                                                <c:forEach items="${rule.sources}" var="aRule">
                                                    <c:if test="${aRule.scale eq scale}">
                                                        <f:message key="rule-${aRule.ruleCompletion}" />
                                                        <c:set var="found" value="${true}" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${!found}">
                                                    <f:message key="rule-NO" />
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                        <td>-&gt;</td>
                                        <c:forEach items="${method.goalSatisfaction}" var="scale">
                                            <td>
                                                <c:set var="found" value="${false}" />
                                                <c:forEach items="${rule.targets}" var="aRule">
                                                    <c:if test="${aRule.scale eq scale}">
                                                        <f:message key="rule-${aRule.ruleCompletion}" />
                                                        <c:set var="found" value="${true}" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${!found}">
                                                    <f:message key="rule-NO" />
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                        <td>
                                            <c:choose>
                                                <c:when test="${!(empty goalRule) && (rule eq goalRule)}">
                                                    <f:message key="na-being-edited" />
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:out value="editgoalrule-${rule.id}.do" />"><f:message key="edit" /></a>
                                                    | <a class="confirmable" href="<c:out value="deleterule-${rule.id}.do" />"><f:message key="delete" /></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        </c:if>
                    </div>
                </c:if>
                <c:if test="${!method.charPracticeImplementation && !method.rateGoalSatisfaction}">
                    <p><f:message key="no-aggregation-rules" /></p>
                </c:if>
            </div>
            <div style="width:300px;">
                <h2><f:message key="finish-method" /></h2>
                <form class="uniForm" action="finish-method.do" method="get">
                    <div class="buttonHolder">
                        <button class="primaryAction" type="submit"><f:message key="finish-method" /></button>
                    </div>
                </form>
            </div>

        </div>
    </body>
</html>
