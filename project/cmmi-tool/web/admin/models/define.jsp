<%-- 
    Document   : form - Manage project
    Created on : 23.7.2009, 22:55:11
    Author     : Lukas Strmiska
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <script type="text/javascript">
<!--
 $(document).ready(function() {

   $("#treeview").treeview({
        url: "treeview.do"
   });

 });
-->
</script>
        <f:message key="define-model" var="title" >
            <f:param value="${model.name}" />
        </f:message>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div style="width:600px">

                <ul id="treeview">
                    <!-- lazy loaded -->
        	</ul>

            </div>
        </div>
    </body>
</html>
