<%-- 
    Document   : characterize - Characterize evidence
    Created on : 27.9.2009, 19:51:10
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <style type="text/css">
            @import "${pageContext.request.contextPath}/resources/css/jquery.live.tree.css";
        </style>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.live.tree.js" type="text/javascript"></script>
        <f:message key="characterize-evidence" var="title" />
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div>
                <form action="characterize.do" method="post">
                    <ul id="modeltree" class="livetree treeview">
                        <strmik:tree node="${practiceTree}" />
                    </ul>
                    <div class="buttonHolder">
                        <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
