<%-- 
    Document   : evidence - Evidence coverage report
    Created on : 3.10.2009, 18:23:18
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <%@include file="/WEB-INF/jspf/treeview.jspf" %>
        <f:message key="page-evidence-coverage-report" var="title" />
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div>
                <f:message key="evidence-coverage-help" />
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
                <div>
                    <form action="${pageContext.request.contextPath}/appraisal/reports/" method="get">
                        <ul id="modeltree" class="filetree">
                            <strmik:tree node="${evidenceTree}" />
                        </ul>
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="ok" /></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
