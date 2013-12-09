<%@page import="pl.edu.pb.adserver.model.UserActions"%>
<%@page import="pl.edu.pb.adserver.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<jstl:if test="${param.register == 'true'}">
    
    <% 
        User n = new User(request.getParameter("email"),
                request.getParameter("password"),
                User.UserType.ADM,
                request.getParameter("name"),
                request.getParameter("surname"),
                request.getParameter("telephone"));
        boolean done = (new UserActions()).register(n);
        request.setAttribute("done", done);
    %>
</jstl:if>
<script>
    var passwd1;
    var passwd2;
    function registerPassword1(passwd)
    {
        passwd1 = passwd;
    }
    function checkPasswords(passwd)
    {
        if (passwd.value != passwd1.value)
            alert("hasla nie sa zgodne!");
    }
</script>
        <h1>Register</h1>
        <jstl:if test="${sessionScope.user == null}">
        <form method="POST">
            <div>
                <div>Email:</div><div><input name="email" /></div>
            </div>
            <div>
                <div>Name:</div><div><input name="name" /></div>
            </div>
            <div>
                <div>Surname:</div><div><input name="surname" /></div>
            </div>
            <div>
                <div>Password:</div><div><input name="password" type="password" onChange="registerPassword1(this)" /></div>
            </div>
            <div>
                <div>Retype password:</div><div><input name="retyped_password" type="password" onChange="checkPasswords(this)" /></div>
            </div>
            <div>
                <div>Telephone:</div><div><input name="telephone" /></div>
            </div>
            <div>
                <input type="hidden" name="register" value="true" />
                <input type="submit" value="register" /> </div>
        </form>
        </jstl:if>
        <jstl:if test="${done}">
            Account already registered!
        </jstl:if>
            

