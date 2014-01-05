<%@page contentType="text/html" pageEncoding="UTF-8"
        import="pl.edu.pb.adserver.model.*"%>
<%@page import="java.util.List" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
function findByName(tag, name)
{
    var list = document.getElementsByTagName(tag);
    for(var i = 0; i < list.length; i++)
        if(list.item(i).attributes.getNamedItem("name").value == name)
            return list.item(i);
    return null;
}

function removeAd(adId)
{
    var req = new XMLHttpRequest();
    req.open("DELETE", "${pageContext.servletContext.contextPath}/ad/id/" + adId);
    req.send();
    location.reload(true);
}

function updateAd(adId)
{
    var req = new XMLHttpRequest();
    req.open("PUT", "${pageContext.servletContext.contextPath}/ad/id/" + adId, false);
    params = "";
    var content = findByName("input", "content_" + adId).value;
    var user = findByName("select", "client_" + adId);
    if(user !== null) user = user.value;
    var category = findByName("select", "category_" + adId).value;
    var orientation = findByName("select", "orientation_"  + adId).value;
    var referer = findByName("input", "referer_"  + adId).value;
    var media = findByName("select", "media_"  + adId).value;
    params += "&category=" + category;
    params += "&user=" + user;
    params += "&content=" + content;
    params += "&orientation=" + orientation;
    params += "&referer=" + referer;
    params += "&media=" + media;
    req.send(params);
    location.reload(true);
}

function addCategory(input)
{
    var base = input.getAttribute("base");
    var name = input.value;
    input.value = "";
    
    var req = new XMLHttpRequest();
    req.open("POST", "${pageContext.servletContext.contextPath}/category", false);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send("&base="+base+"&name="+name);
    
    location.reload();
}
</script>

<div class="manager">
    <table>
        <thead>
            <td>user</td>
            <td>referer</td>
            <td>content</td>
            <td>category</td>
            <td>orientation</td>
            <td>media</td>
            <td>views</td>
            <td>clicks</td>
            <td>actions</td>
        </thead>
        <jstl:forEach items="${ads}" var="ad">
        <tr>
            <td>
                <jstl:choose>
                    <jstl:when test="${sessionScope.user.credencials == 'ADM'}">
                        
                        <select name="client_${ad.id}"
                                  value ='${ad.user.email}'>
                            <jstl:forEach items="${users}"  var="user">
                                <jstl:choose>
                                    <jstl:when test="${ad.user.email == user.email}">
                                        <option value="${user.email}" selected="selected">${user.email}</option>
                                    </jstl:when>
                                    <jstl:otherwise>
                                        <option value="${user.email}" >${user.email}</option>
                                    </jstl:otherwise>
                                </jstl:choose>
                            </jstl:forEach>
                        </select>
                    </jstl:when>
                    <jstl:otherwise>
                        <jstl:out value="${ad.user.email}" />
                    </jstl:otherwise>
                </jstl:choose>
            </td>
             <td><input name="referer_${ad.id}" value = "${ad.referer}"/></td>
            <td><input name="content_${ad.id}" value = "${ad.content}"/></td>
            <td>
                <select name="category_${ad.id}"/>'
                <jstl:set var="selected" value="${ad.category.name}" scope="request" />            
                    <%@include file="categories.jsp"%>
                </select>
            </td>
            <td>
                <select name="orientation_${ad.id}"/>'          
                    <option <jstl:if test="${ad.orientation == 'horizontal'}" >selected="selected"</jstl:if>>horizontal</option>
                    <option <jstl:if test="${ad.orientation == 'vertical'}" >selected="selected"</jstl:if>>vertical</option>
                </select>
            </td>
            <td>
                <select name="media_${ad.id}"/>'          
                    <option <jstl:if test="${ad.contentType == 'html'}" >selected="selected"</jstl:if>>html</option>
                    <option <jstl:if test="${ad.contentType == 'picture'}" >selected="selected"</jstl:if>>picture</option>
                </select>
            </td>
            <td>${ad.views}</td>
            <td>${ad.clicks}</td>
            <td>
                <button onClick="updateAd(${ad.id})">
                    Update
                </button>
                <button onClick="removeAd(${ad.id})">
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
                <td>referer</td>
                <td>content</td>
                <td>category</td>
                <td>media type</td>
            </thead>
            <tr>
                <td><input type="text" multiple="true"  name="user" /></td>
                <td><input type="text" multiple="true"  name="referer" /></td>
                <td><input type="text" multiple="true"  name="content" /></td>
                <td>
                    <select name="category">
                        <%@include file="categories.jsp"%>
                    </select>
                </td>
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
    <% 
    CategoryBuilder cb = null;
    if(!((User)(request.getSession().getAttribute("user"))).getCredencials().equals("ADM"))
        cb = new CategoryBuilder((Category)request.getAttribute("rootCategory"));
    else
        cb = new CategoryBuilder((Category)request.getAttribute("rootCategory"), 1);
    request.setAttribute("cb", cb);
    %>
Available categories:
${cb.result}

</div>