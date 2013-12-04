<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<script>

function deleteUser(userId)
{
    var req = new XMLHttpRequest();
    req.open("DELETE", "/AdServer/user/id/" + userId, false);
    location.reload(true);
}

function updateUser(userId)
{
    var req = new XMLHttpRequest();
    req.open("PUT", "/AdServer/user/id/" + userId, false);
    
    var name = findByName("input", "name_" + userId).value;
    var email = findByName("input", "email_" + userId).value;
    var surname = findByName("input", "surname_" + userId).value;
    var telephone = findByName("input", "telephone_" + userId).value;
    var password = findByName("input", "password_" + userId).value;
    var creden = findByName("select", "credencials_" + userId).value;
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send("name="+name +"&surname=" +surname+"&telephone="+telephone+
            "&password="+password+"&email="+email + "&credencials=" + creden);
    location.reload(true);
}

function findByName(tag, name)
{
    var list = document.getElementsByTagName(tag);
    for(var i = 0; i < list.length; i++)
        if(list.item(i).attributes.getNamedItem("name").value == name)
            return list.item(i);
}
</script>
<jstl:if test='${user.credencials != "ADM"}'>
    <jstl:redirect url="/" />
</jstl:if>
<table border="1px">
    <thead>
        <td>First Name</td>
        <td>Second Name</td>
        <td>E-Mail</td>
        <td>Telephone</td>
        <td>Password</td>
        <td>Status</td>
        <td>Actions</td>
    </thead>
    <jstl:forEach items="${users}" var="user">
        <tr>
            <td>
                <input type="text" name="name_<jstl:out value='${user.id}' />"
                       value="<jstl:out value='${user.first_name}' />" />
            </td>
            <td>
                <input type="text" name="surname_<jstl:out value='${user.id}' />"
                       value="<jstl:out value='${user.second_name}' />" />
            </td>
            <td>
                <input type="text" name="email_<jstl:out value='${user.id}' />"
                       value="<jstl:out value='${user.email}' />" />
            </td>
            <td>
                <input type="text" name="telephone_<jstl:out value='${user.id}' />"
                       value="<jstl:out value='${user.telephone}' />" />
            </td>
            <td>
                <input type="password" name="password_<jstl:out value='${user.id}' />"
                       value="<jstl:out value='${user.password}' />" />
            </td>
            <td>
                <select name="credencials_<jstl:out value='${user.id}' />">
                    <option value="CLI"
                            <jstl:if test="${user.credencials == 'CLI'}">selected="selected"</jstl:if>
                    >client</option>
                    <option value="ADM" 
                        <jstl:if test="${user.credencials == 'ADM'}">selected="selected"</jstl:if>
                    >administrator</option>
                    <option value="PAR" 
                        <jstl:if test="${user.credencials == 'PAR'}">selected="selected"</jstl:if> 
                    >partner</option>
                </select>
            </td>
            <td>
                <button onClick="deleteUser(<jstl:out value='${user.id}' />)">Remove</button>
                <button onClick="updateUser(<jstl:out value='${user.id}' />)">Update</button>
            </td>
        </tr>
    </jstl:forEach>
</table>
