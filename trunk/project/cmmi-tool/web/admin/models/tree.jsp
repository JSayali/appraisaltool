<%-- 
    Document   : tree
    Created on : 24.7.2009, 20:11:21
    Author     : Lukas Strmiska
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
[
<c:forEach items="${areas}" var="area" varStatus="vsa">
    {
        "text" : "${area.id}: ${area.name}"
        <c:if test="${!(empty area.goals)}">
            ,"children": [
            <c:forEach items="${area.goals}" var="goal" varStatus="vsg">
                {
                    "text" : "${goal.id}: ${goal.name}"
                    <c:if test="${!(empty goal.practices)}">
                        ,"children":[
                        <c:forEach items="${goal.practices}" var="practice" varStatus="vsp">
                            {
                                "text" : "${practice.id}: ${practice.name}"
                            }<c:if test="${!vsp['last']}">,</c:if>
                        </c:forEach>
                        ]
                    </c:if>

                }<c:if test="${!vsg['last']}">,</c:if>
            </c:forEach>
            ]
        </c:if>
    }<c:if test="${!vsa['last']}">,</c:if>
</c:forEach>
]