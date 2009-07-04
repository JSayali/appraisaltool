<%-- 
    Document   : dashboard
    Created on : 20.6.2009, 21:26:57
    Author     : lucas
--%>

<%@include file="/WEB-INF/jspf/logged-in.jspf" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><f:message key="admin-dashboard" /></title>
    </head>
    <body>
        <h1><f:message key="admin-dashboard" /></h1>
        <h2><f:message key="user-management" /></h2>
        <ul>
            <li><a href="users/add.do"><f:message key="add-user" /></a></li>
            <li><a href="users/manage.do"><f:message key="user-management" /></a></li>
        </ul>
    </body>
</html>
