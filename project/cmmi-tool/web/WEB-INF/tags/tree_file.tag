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
<%@attribute name="color" required="false" %>

<li>
    <span class="${node.type}">
        <c:if test="${!node.haveLists}">
            <table border="0">
        </c:if>
        <c:if test="${node.haveLists}">
            <table border="1" style="border-collapse: collapse">
        </c:if>
            <tr>
        <c:if test="${color}">
            <td bgcolor="${node.color}">&nbsp;&nbsp;&nbsp;</td>
        </c:if>

                <c:if test="${!node.haveLists}">
                <td>
        </c:if>
        <c:if test="${node.haveLists}">
                <td width="300px">
        </c:if>
                    <c:if test="${!(empty node.checkbox)}">
                        <input type="checkbox" name="${node.checkbox}" <c:if test="${node.checked}">checked="checked"</c:if> />
                    </c:if>
                    <c:if test="${!(empty node.link)}">
                        <a href="${node.link}">
                        </c:if>
                        <c:out value="${node.label}" />
                        <c:if test="${!(empty node.link)}">
                        </a>
                    </c:if>
                <c:if test="${!(empty node.removeLink)}">
                    (<a href="${node.removeLink}" class="confirmable">X</a>)
                </c:if>
                </td>

                <c:if test="${node.haveLists}">
                    <c:forEach items="${node.lists}" var="entry">
                        <td>
                            <c:out value="${node.listLabels[entry.key]}" />
                            <select name="${entry.key}">
                                <c:forEach items="${entry.value}" var="option">
                                    <option value="${option.key}" <c:if test="${node.listValues[entry.key] eq option.key}">selected="selected"</c:if>>
                                    <c:out value="${option.value}" /></option>
                                </c:forEach>
                            </select>
                        </td>
                        </c:forEach>
                    </c:if>
            </tr>
        </table>
    </span>
    <c:if test="${!(empty node.subNodes)}">
        <ul>
            <c:forEach items="${node.subNodes}" var="subNode">
                <strmik:tree node="${subNode}" color="${color}" />
            </c:forEach>
        </ul>
    </c:if>
</li>