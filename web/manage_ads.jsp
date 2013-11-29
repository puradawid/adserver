<%@page contentType="text/html" pageEncoding="UTF-8"
        import="pl.edu.pb.adserver.model.*, pl.edu.pb.adserver.model.jdbc.*"%>
<%@page import="java.util.List" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- here will be managing for advertisments -
     - show all available
     - update them
     - delete
     All users (client privillidged) can have own ads and manage them. Also there
     is a admin user who can manage whole of them with manipulate their content.
     (QuickFix manage help).
-->
<jstl:if test="${sessionScope.credencials == 'ADM' || sessionScope.credencials == 'CLI'}">
    <% List<Ad> ads = AdJdbc.getAllAds(); %>
<div class="manager">
    <table>
        <jstl:forEach items="${ads}" var="ad">
        <tr>
            <td><jstl:out value="${ad.getContent()}" /></td>
            <td></td>
        </tr>
        </jstl:forEach>
    </table>
    

</div>
</jstl:if>