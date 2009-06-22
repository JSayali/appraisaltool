<%-- 
    Document   : userForm
    Created on : 22.6.2009, 22:08:22
    Author     : lucas
--%>

<%@include file="/WEB-INF/jspf/logged-in.jspf" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><f:message key="user-management" /></title>
    </head>
    <body>
        <h1>
            <f:message key="user-management" />:
            <c:if test="${new}">
                <f:message key="add-user" />
            </c:if>
            <c:if test="${!new}">
                <f:message key="edit-user" />
            </c:if>
        </h1>
        <form:form commandName="user">
            <form:errors path="*" cssClass="errorBox" />
            <table>
                <tr>
                    <td>Login:</td>
                    <td><form:input path="id" /></td>
                    <td><form:errors path="id" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="password" /></td>
                    <td><form:errors path="password" /></td>
                </tr>
                <tr>
                    <td>Password (retype):</td>
                    <td><form:password path="password2" /></td>
                    <td><form:errors path="password2" /></td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><form:input path="name" /></td>
                    <td><form:errors path="name" /></td>
                </tr>
                <tr>
                    <td>E-mail:</td>
                    <td><form:input path="email" /></td>
                    <td><form:errors path="email" /></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <form:select path="role">
                            <form:option value="-" label="-- <f:message key=\"please-select\" />"/>
                            <form:options items="${roleList}" />
                        </form:select>
                    </td>
                    <td><form:errors path="role" /></td>
                </tr>
                <%--
                <tr>
                    <td>Not expired:</td>
                    <td><form:checkbox path="nonExpired"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>Not locked:</td>
                    <td><form:checkbox path="nonLocked"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>Enabled:</td>
                    <td><form:checkbox path="enabled"/></td>
                    <td>&nbsp;</td>
                </tr>
                --%>
                <tr>
                    <td colspan="3">
                        <input type="submit" value="<f:message key="save-changes" />" />
                    </td>
                </tr>
            </table>
            <input name="new" type="hidden" value="${new}"/>
        </form:form>
    </body>
</html>
