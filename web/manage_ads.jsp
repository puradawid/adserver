<%@page contentType="text/html" pageEncoding="UTF-8"
        import="pl.edu.pb.adserver.model.*, pl.edu.pb.adserver.model.jdbc.*"%>
<%@page import="java.util.List" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
function findByName(tag, name)
{
    var list = document.getElementsByTagName(tag);
    for(var i = 0; i < list.length; i++)
        if(list.item(i).attributes.getNamedItem("name").value == name)
            return list.item(i);
}

function removeAd(adId)
{
    var req = new XMLHttpRequest();
    req.open("DELETE", "${pageContext.servletContext.contextPath}/ad/id/" + adId);
    req.send();
}

function updateAd(adId)
{
    var req = new XMLHttpRequest();
    req.open("PUT", "${pageContext.servletContext.contextPath}/ad/id/" + adId);
    params = "";
    var content = findByName("input", "content_" + adId).value;
    var user = findByName("input", "client_" + adId).value;
    var category = findByName("input", "category_" + adId).value;
    params += "&category=" + category;
    params += "&user=" + user;
    params += "&content=" + content;
    req.send(params);
}
</script>

<div class="manager">
    <table>
        <thead>
            <td>user</td>
            <td>content</td>
            <td>category</td>
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
            <td><input name="category_<jstl:out value='${ad.id}' />"
                                  value ='<jstl:out value="${ad.category}" />'/></td>
            <td>
                <button onClick="updateAd(<jstl:out value='${ad.id}' />)">
                    Update
                </button>
                <button onClick="removeAd(<jstl:out value='${ad.id}' />)">
                    Remove
                </button>
            </td>
        </tr>
        </jstl:forEach>
    </table>
    <h1>Add another ad</h1>
    <form method="POST">
        <table>
            <thead>
                <td>user</td>
                <td>category</td>
                <td>content</td>
            </thead>
            <tr>
                <td><input type="text" multiple="true"  name="user" /></td>
                <td><input type="text" multiple="true"  name="content" /></td>
                <td><input type="text" name="category" /></td>
                        <td>
                            <select name="content_type">
                                <option value="html">html</option>
                                <option value="picture">picture</option>
                            </select>
                        </td>
                
            </tr>
            <tr><td><input type="submit" value="Register"/></td></tr>
        </table>
    </form>

</div>