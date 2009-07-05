<%-- 
    Document   : admin dashboard
    Created on : 20.6.2009, 21:26:57
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="admin-dashboard" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><f:message key="admin-dashboard" /></h1>
            <h2><f:message key="user-management" /></h2>
            <ul>
                <li><a href="users/"><f:message key="user-management" /></a></li>
            </ul>
        </div>
    </body>
</html>
