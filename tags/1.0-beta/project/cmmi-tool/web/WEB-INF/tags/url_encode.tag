<%-- 
    Document   : url_encode
    Created on : 13.9.2009, 18:06:01
    Author     : Lukas Strmiska
--%>

<%@tag description="encodes url" pageEncoding="UTF-8"%>
<%@tag import="java.net.URLEncoder" %>

<%@attribute name="value" required="true" %>

<%
try {
    String encodedurl = URLEncoder.encode(value, "UTF-8");
    out.println(encodedurl);
} catch(Exception e) {
    throw new RuntimeException(e);
}
%>