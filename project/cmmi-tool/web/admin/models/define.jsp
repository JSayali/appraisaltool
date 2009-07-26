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
                    cookieId: "modeltree"
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
            <div style="width:600px">
                <div>
                    <button onclick="addprocess('processarea')"><f:message key="add-processarea" /></button>
                    <button onclick="addprocess('goal')" <c:if test='${!ableAddGoal}'>disabled="disabled"</c:if>><f:message key="add-goal" /></button>
                    <button onclick="addprocess('practice')" <c:if test='${!ableAddPractice}'>disabled="disabled"</c:if>><f:message key="add-practice" /></button>
                    <button onclick="addprocess('artifact')" <c:if test='${!ableAddArtifact}'>disabled="disabled"</c:if>><f:message key="add-artifact" /></button>
                </div>
                <div>
                    <ul id="modeltree" class="filetree">
                        <strmik:tree node="${modelTree}" />
                    </ul>
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
