<%-- 
    Document   : link - Map evidence to practice
    Created on : 27.9.2009, 15:33:12
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

                $("#modeltree").treeview({
                    persist: "cookie",
                    cookieId: "modeltree",
                    control: "#treecontrol"
                });

            });
            -->
        </script>
        <f:message key="map-evidence" var="title" >
            <f:param value="${evidence.name}" />
        </f:message>
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
                <div>
                    <form action="link.do" method="post">
                        <ul id="modeltree" class="filetree">
                            <strmik:tree node="${practiceTree}" />
                        </ul>
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="next-step" /></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
