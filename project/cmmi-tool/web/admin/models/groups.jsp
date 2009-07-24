<%-- 
    Document   : groups - Manage model groups
    Created on : 24.7.2009, 21:21:19
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <f:message key="manage-model-groups" var="title" >
             <f:param value="${model.name}" />
        </f:message>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div style="width:600px">
                <form:form commandName="group" cssClass="uniForm" action="add-group.do">
                    <fieldset class="inlineLabels">
                        <legend><f:message key="add-new-process-group" /></legend>
                        <c:if test="${!empty saved}">
                            <div id="OKMsg">
                                <p><f:message key="groups-saved" /></p>
                            </div>
                        </c:if>

                        <strmik:inputText object="group" property="name" />

                        <div class="buttonHolder">
                            <button class="resetButton" type="reset"><f:message key="reset" /></button>
                            <button class="primaryAction" type="submit"><f:message key="add-new-process-group" /></button>
                        </div>
                    </fieldset>
                </form:form>
                <h4><f:message key="current-process-groups" /></h4>
                <table>
                    <thead>
                        <tr>
                            <td><f:message key="process-group" /></td><td><f:message key="actions" /></td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${model.processGroups}" var="group">
                            <tr>
                                <td><c:out value="${group.name}"/></td>
                                <td><a class="confirmable" href="<c:out value="remove-group-${group.id}.do" />">
                                    <f:message key="delete" /></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <form method="get" action="define-model.do" class="uniForm">
                    <fieldset class="inlineLabels">
                        <legend />
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="next-step" /></button>
                        </div>
                    </fieldset>
                </form>
            </div>

        </div>
    </body>
</html>
