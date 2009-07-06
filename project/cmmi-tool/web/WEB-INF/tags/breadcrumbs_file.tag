<%-- 
    Document   : breadcrumbs_file
    Created on : 5.7.2009, 15:27:31
    Author     : Lukas Strmiska
--%>

<%@tag description="creates breadcrumbs" pageEncoding="UTF-8"%>
<%@tag import="cz.strmik.cmmitool.web.lang.LangProvider" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="divider"  %>

<%
String requestPath = (String) request.getAttribute("javax.servlet.forward.request_uri");
if(divider == null) {
    divider = " &raquo; ";
}

if(requestPath!=null) {
    String contextPath = request.getContextPath();
    requestPath = requestPath.substring(contextPath.length());
    StringBuilder result = new StringBuilder();
    result.append("<a href=\"");
    result.append(contextPath);
    result.append("\">");
    result.append(LangProvider.getString("page-home"));
    result.append("</a>");
    result.append(divider);
    String[] urls = requestPath.split("/");
    String lastUrl = contextPath + "/";
    for(int i=0;i<urls.length - 1;i++) {
        if(urls[i].equals("")) {
            continue;
        }
        result.append("<a href=\"");
        lastUrl = lastUrl + urls[i] + "/";
        result.append(lastUrl);
        result.append("\">");
        result.append(LangProvider.getString("page-"+urls[i]));
        result.append("</a>");
        result.append(divider);
    }
    out.println(result.toString());
}

%>