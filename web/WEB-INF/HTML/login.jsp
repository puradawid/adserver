<%@page import="pl.edu.pb.adserver.model.UserActions"%>
<%@page import="pl.edu.pb.adserver.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.logging.Logger,javax.servlet.http.*, java.security.*, java.sql.*"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<jstl:if test="${param.action == 'login'}">
    <% User logged = (new UserActions()).login(request.getParameter("login"),
            request.getParameter("password")); %>
    <% if(logged != null)  
        request.getSession().setAttribute("user", logged); %>
        <jstl:if test="${sessionScope.user == null}">
            Sorry but we don't recognize you as our user.
        </jstl:if>
    <%--
    <sql:query var="user" dataSource="jdbc/adserver_database">
         SELECT credencials FROM users WHERE email =
        '<jstl:out value="${param.login}"/>' AND password = MD5('<jstl:out value="${param.password}"/>') LIMIT 1;
    </sql:query>
        
    <sql:query var="howMany" dataSource="jdbc/adserver_database">
        SELECT COUNT(*) FROM users WHERE email =
        '<jstl:out value="${param.login}"/>' AND password = MD5('<jstl:out value="${param.password}"/>') LIMIT 1;
    </sql:query>
    There is no user like that, sorry :(
    <jstl:if test="${howMany != null && howMany.getRowsByIndex()[0][0] == 1}">
        <jstl:set scope="session" var="user" property="user" value="${param.login}" />
        <jstl:set scope="session" var="credencials" property="credencials" value="${user.getRows()[0].get('credencials')}" />
        <jstl:out value="${sessionScope.credencials}" />
    </jstl:if>
    --%>
</jstl:if>

<%
HttpSession sn = request.getSession();
//some code for login handling
if(sn.getAttribute("user") != null)
{
    response.sendRedirect("");
}
%>

    
<jstl:if test="${sessionScope.user == null}">
    <h1>Login</h1>    
    <form method="POST">
            <div>
                <div>Login:</div>
                <div><input name="login" placeholder="user@example.com" /> </div>
            </div>
            <div>
                <div>Password:</div>
                <div><input name="password" type="password" placeholder="password1234" /> </div>
            </div>
            <div>
                <input type='hidden' value='login' name='action' />
                <input type="submit" value="Login" />
            </div>
        </form>
</jstl:if>