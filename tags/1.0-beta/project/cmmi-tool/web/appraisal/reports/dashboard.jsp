<%-- 
    Document   : reports review
    Created on : 3.10.2009, 18:11:00
    Author     : Lukas Strmiska
--%>

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="page-reports" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><f:message key="page-reports" /></h1>
            <ul>
                <li><a href="evidence.do"><f:message key="page-evidence-coverage-report" /></a></li>
            </ul>
        </div>
    </body>
</html>
