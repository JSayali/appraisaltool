<%-- 
    Document   : options_file
    Created on : 6.7.2009, 18:06:35
    Author     : lucas
--%>

<%@tag description="tree tag" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="f" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="strmik" uri="/WEB-INF/tlds/strmiktags_library" %>

<%@attribute name="node" required="true" type="cz.strmik.cmmitool.util.tree.TreeNode" %>

<li>
    <span class="${node.type}">
        <c:if test="${!(empty node.link)}">
            <a href="${node.link}">
            </c:if>
            <c:out value="${node.label}" />
            <c:if test="${!(empty node.link)}">
            </a>
        </c:if>
    </span>
    <c:if test="${!(empty node.subNodes)}">
        <ul>
            <c:forEach items="${node.subNodes}" var="subNode">
                <strmik:tree node="${subNode}" />
            </c:forEach>
        </ul>
    </c:if>
</li>