<%-- 
    Document   : appraisal dashboard
    Created on : 20.6.2009, 21:27:09
    Author     : Lukas Strmiska
--%>

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="conduct-appraisal" /></title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/logged-in.jspf" %>
        <div id="content">
            <h1><f:message key="conduct-appraisal" /></h1>
            <div style="width:600px">
                <form class="uniForm" action="" method="post">
                    <fieldset class="blockLabels">
                        <legend><f:message key="choose-project-first" /></legend>
                        <div class="ctrlHolder">
                            <label for=projectId">
                                <f:message key="project" />
                            </label>

                            <select name="projectId">
                                <option value="">-- <f:message key="please-select" /></option>
                                <c:forEach items="${projects}" var="proj">
                                    <c:if test="${project.id eq proj.id}">
                                        <option selected="selected" value="${proj.id}"><c:out value="${proj.name}" /></option>
                                    </c:if>
                                    <c:if test="${!(project.id eq proj.id)}">
                                        <option value="${proj.id}"><c:out value="${proj.name}" /></option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="buttonHolder">
                            <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                        </div>
                    </fieldset>
                </form>
            </div>
            <div>
                <c:if test="${!(empty project)}">
                    <h3><f:message key="project" />: <c:out value="${project.name}"/></h3>
                    <ul>
                        <li><a href="evidence/"><f:message key="manage-evidence" /></a></li>
                        <c:if test="${auditor}">
                            <li><a href="rate/"><f:message key="page-rate" /></a></li>
                        </c:if>
                        <li><a href="reports/"><f:message key="page-reports" /></a></li>
                    </ul>
                </c:if>
            </div>
        </div>
    </body>
</html>
