<%-- 
    Document   : projectForm3 - Manage project process instatiations
    Created on : 18.10.2009, 17:12:10
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title>
            <f:message key="manage-process-instantiations"  var="title">
                <f:param value="${project.name}" />
            </f:message>
            <c:out value="${title}" />
        </title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1>
                <c:out value="${title}" />
            </h1>
                <div style="width:600px">
                    <c:if test="${pi.new}">
                        <c:set var="command" value="add-pi.do" />
                    </c:if>
                    <c:if test="${!pi.new}">
                        <c:set var="command" value="save-pi-${pi.id}.do" />
                    </c:if>
                    <form:form commandName="pi" cssClass="uniForm" action="${command}">
                        <fieldset class="inlineLabels">
                            <legend>
                            <c:if test="${pi.new}">
                                <f:message key="add-new" />
                            </c:if>
                            <c:if test="${!pi.new}">
                                <f:message key="edit" />
                            </c:if>
                        </legend>
                        <c:if test="${!empty saved}">
                            <div id="OKMsg">
                                <p><f:message key="saved" /></p>
                            </div>
                        </c:if>

                        <strmik:inputText object="pi" property="name" />
                        <strmik:inputTextArea object="pi" property="context" />

                        <div class="buttonHolder">
                            <button class="resetButton" type="reset"><f:message key="reset" /></button>
                            <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                        </div>
                    </fieldset>
                </form:form>
                <h4><f:message key="current-pis" /></h4>
                <table class="tablesorter">
                    <thead>
                        <tr>
                            <th><f:message key="name" /></th><th><f:message key="context" /></th><th><f:message key="actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${project.instantions}" var="inst">
                            <tr>
                                <td><c:out value="${inst.name}"/></td>
                                <td><c:out value="${inst.context}"/></td>
                                <td>
                                    <a href="<c:out value="inst-edit-${inst.id}.do" />"><f:message key="edit" /></a>
                                    <c:if test="${!inst.defaultInstantiation}">
                                        | <a class="confirmable" href="<c:out value="remove-inst-${inst.id}.do" />">
                                        <f:message key="delete" /></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <form method="get" action="finish-project.do" class="uniForm">
                    <fieldset class="inlineLabels">
                        <legend />
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="finish" /></button>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </body>
</html>
