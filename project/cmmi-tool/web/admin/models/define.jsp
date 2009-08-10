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

                $("#modeltree").treeview({
                    persist: "cookie",
                    cookieId: "modeltree",
                    control: "#treecontrol"
                });

            });

            function addprocess(type) {
                switch(type) {
                    case "processarea" : formtitle = "<f:message key='add-processarea' />";break;
                        case "goal" : formtitle = "<f:message key='add-goal' />";break;
                            case "practice" : formtitle = "<f:message key='add-practice' />";break;
                                case "artifact" : formtitle = "<f:message key='add-artifact' />";break;
                                }
                                $("#form_legend").html(formtitle);
                                $("#form").attr("action", "add-"+type+".do")
                                var frmwin = new DialogWindow($("#add_form_container").html(), {title: formtitle, hideOkButton: true});
                                frmwin.show();
                            }
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
            <div>
                <div>
                    <button onclick="addprocess('processarea')"><f:message key="add-processarea" /></button>
                    <button onclick="addprocess('goal')" <c:if test='${!ableAddGoal}'>disabled="disabled"</c:if>><f:message key="add-goal" /></button>
                    <button onclick="addprocess('practice')" <c:if test='${!ableAddPractice}'>disabled="disabled"</c:if>><f:message key="add-practice" /></button>
                    <button onclick="addprocess('artifact')" <c:if test='${!ableAddArtifact}'>disabled="disabled"</c:if>><f:message key="add-artifact" /></button>
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
                </div>
                <div style="float: left;">
                    <c:if test="${!displayForm}">
                        <f:message key="please-select-node" />
                    </c:if>
                    <c:if test="${displayForm}">
                        <form:form commandName="node" cssClass="uniForm" action="save-${node.class.simpleName}-${node.id}.do">
                            <spring:hasBindErrors htmlEscape="false" name="node">
                                <div id="errorMsg">
                                    <h3><f:message key="invalid-input" /></h3>
                                </div>
                            </spring:hasBindErrors>
                            <fieldset class="inlineLabels">

                                <legend>
                                    <c:if test="${ableAddGoal}">
                                        <f:message key="process-area-details" />
                                    </c:if>
                                    <c:if test="${ableAddArtifact}">
                                        <f:message key="practice-details" />
                                    </c:if>
                                    <c:if test="${ableAddPractice}">
                                        <f:message key="goal-details" />
                                    </c:if>
                                    <c:if test="${artifactEdit}">
                                        <f:message key="artifact-details" />
                                    </c:if>
                                </legend>
                                <c:if test="${!empty saved}">
                                    <div id="OKMsg">
                                        <p><f:message key="saved" /></p>
                                    </div>
                                </c:if>

                                <strmik:inputText object="node" property="acronym" />
                                <strmik:inputText object="node" property="name" />
                                <c:if test="${ableAddGoal}">
                                    <strmik:options object="node" property="processGroup" items="${model.processGroups}" title="process-group" itemLabel="name" />
                                    <strmik:options object="node" property="maturityLevel" items="${levels1}" title="process-maturity" />
                                </c:if>
                                <c:if test="${ableAddArtifact}">
                                    <strmik:options object="node" property="practiceCapability" items="${levels0}" title="practice-capability" />
                                </c:if>

                                <c:if test="${!artifactEdit}">
                                    <strmik:inputTextArea object="node" property="summary" cols="60" />
                                    <c:if test="${!ableAddPractice}">
                                        <strmik:inputTextArea object="node" property="purpose" />
                                    </c:if>
                                </c:if>
                                <c:if test="${artifactEdit}">
                                    <div class="ctrlHolder">
                                        <label class="inlineLabel" for="direct">
                                            <form:checkbox id="direct" path="direct"/>
                                            <span><f:message key="direct-artifact" /></span>
                                        </label>
                                    </div>
                                </c:if>

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

        <!-- div for popup forms -->
        <div style="display: none;" id="add_form_container">
            <form id="form" action="" class="uniForm" method="get">
                <fieldset class="inlineLabels">
                    <legend id="form_legend"></legend>
                    <div class="ctrlHolder">
                        <label for="acronym"><f:message key="acronym" /></label>
                        <input id="acronym" class="textInput" type="text" maxlength="50" size="35" value="" name="acronym"/>
                    </div>
                    <div class="ctrlHolder">
                        <label for="elementName"><f:message key="name" /></label>
                        <input id="elementName" class="textInput" type="text" maxlength="50" size="35" value="" name="elementName"/>
                    </div>
                    <div class="buttonHolder">
                        <button class="resetButton" type="reset"><f:message key="reset" /></button>
                        <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                    </div>
                </fieldset>
            </form>
        </div>

    </body>
</html>
