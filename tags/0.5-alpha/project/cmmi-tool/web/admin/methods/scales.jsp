<%-- 
    Document   : scales - Define rating scales
    Created on : 12.8.2009, 21:52:14
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
        <f:message key="define-scales" var="title" >
            <f:param value="${method.name}" />
        </f:message>
        <title><c:out value="${title}" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><c:out value="${title}" /></h1>
            <div>
                <div>
                    <button onclick="location.href='add-scale.do'" <c:if test='${!ableAddScale}'>disabled="disabled"</c:if>><f:message key="add-scale" /></button>
                </div>
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
                <div style="width:400px; float: left;">
                    <ul id="modeltree" class="filetree">
                        <strmik:tree node="${modelTree}" />
                    </ul>
                    <div style="width:300px;">
                        <form class="uniForm" action="aggregation.do" method="get">
                            <div class="buttonHolder">
                                <button class="primaryAction" type="submit"><f:message key="next-step" /></button>
                            </div>
                        </form>
                    </div>
                </div>
                <div style="float: left;">
                    <c:if test="${!displayForm}">
                        <f:message key="please-select-scale" />
                    </c:if>
                    <c:if test="${displayForm}">
                        <form:form commandName="scale" cssClass="uniForm">
                            <spring:hasBindErrors htmlEscape="false" name="node">
                                <div id="errorMsg">
                                    <h3><f:message key="invalid-input" /></h3>
                                </div>
                            </spring:hasBindErrors>
                            <fieldset class="inlineLabels">

                                <legend>
                                    <f:message key="scale-details" />
                                </legend>
                                <c:if test="${!empty saved}">
                                    <div id="OKMsg">
                                        <p><f:message key="saved" /></p>
                                    </div>
                                </c:if>

                                <strmik:inputText object="scale" property="name" />
                                <strmik:inputText object="scale" property="score" />
                            </fieldset>

                            <div class="buttonHolder">
                                <button class="resetButton" type="reset"><f:message key="reset" /></button>
                                <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                            </div>

                        </form:form>
                    </c:if>

                </div>
            </div>
        </div>

    </body>
</html>
