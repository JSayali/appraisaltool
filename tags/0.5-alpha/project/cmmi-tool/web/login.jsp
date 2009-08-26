<%-- 
    Document   : login
    Created on : 22.7.2009, 18:01:31
    Author     : Lukas Strmiska
--%>

<html>
    <head>
        <%@include file="/WEB-INF/jspf/init.jspf" %>
        <title><f:message key="app-title-login" /></title>
    </head>
    <body onload="document.f.j_username.focus();">

        <h1><f:message key="app-title-login" /></h1>

        <div style="width: 600px">

            <form class="uniForm" name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
                <c:if test="${not empty param.login_error}">
                    <div id="errorMsg">
                        <h3><f:message key="unsucessfull-login" >
                                <f:param value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
                            </f:message></h3>
                    </div>
                </c:if>

                <fieldset class="inlineLabels">
                    <legend><f:message key="login-details" /></legend>

                    <div class="ctrlHolder <c:if test="${not empty param.login_error}">error</c:if>">
                        <label for="j_username" ><f:message key="username" /></label>
                        <input type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                    </div>

                    <div class="ctrlHolder <c:if test="${not empty param.login_error}">error</c:if>">
                        <label for="j_password" ><f:message key="password" /></label>
                        <input type='password' name='j_password' />
                    </div>

                    <div class="ctrlHolder">
                        <label for="_spring_security_remember_me" ><f:message key="remember-me" /></label>
                        <input type='checkbox' name='_spring_security_remember_me' />
                    </div>

                </fieldset>
                <div class="buttonHolder">
                    <button class="resetButton" type="reset"><f:message key="reset" /></button>
                    <button class="primaryAction" type="submit"><f:message key="submit" /></button>
                </div>

            </form>
        </div>
    </body>
</html>

