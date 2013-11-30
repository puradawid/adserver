<%@page contentType="text/html" pageEncoding="UTF-8"
        import="pl.edu.pb.adserver.model.*, pl.edu.pb.adserver.model.jdbc.*"%>
<%@page import="java.util.List" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="manager">
    <table>
        <thead>
            <td>user</td>
            <td>category</td>
            <td>content</td>
            <td>actions</td>
        </thead>
        <jstl:forEach items="${ads}" var="ad">
        <tr>
            <td>
                <jstl:choose>
                    <jstl:when test="${sessionScope.user.credencials == 'ADM'}">
                        <input name="client_<jstl:out value='${ad.id}' />"
                                  value ='<jstl:out value="${ad.user.email}" />'/>
                    </jstl:when>
                    <jstl:otherwise>
                        <jstl:out value="${ad.user.email}" />
                    </jstl:otherwise>
                </jstl:choose>
            </td>
            <td><input name="content_<jstl:out value='${ad.id}' />"
                                  value ='<jstl:out value="${ad.content}" />'/></td>
            <td><input name="client_<jstl:out value='${ad.id}' />"
                                  value ='<jstl:out value="${ad.user.email}" />'/></td>
        </tr>
        </jstl:forEach>
    </table>
    

</div>