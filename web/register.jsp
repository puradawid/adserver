<%@page import="pl.edu.pb.adserver.model.UserActions"%>
<%@page import="pl.edu.pb.adserver.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<jstl:if test="${param.register == 'true'}">
    
    <% 
        User n = new User(request.getParameter("email"),
                request.getParameter("password"),
                User.UserType.CLI,
                request.getParameter("name"),
                request.getParameter("surname"),
                request.getParameter("telephone"));
        (new UserActions()).register(n);
    %>
    
    <jstl:catch var="sqlexception" >
    <sql:update dataSource="jdbc/adserver_database" var="result">
        INSERT INTO users (first_name, email, password, telephone, second_name)
        VALUES (
            '<jstl:out value="${param.name}"/>',
            '<jstl:out value="${param.email}"/>',
            MD5('<jstl:out value="${param.password}"/>'),
            '<jstl:out value="${param.telephone}"/>',
            '<jstl:out value="${param.second_name}"/>'
            );
    </sql:update>
    </jstl:catch>
    <jstl:if test="${sqlexception != null}">
        User already registerd or there is a bug with form
    </jstl:if>
    <jstl:redirect url="/" />
</jstl:if>

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
                <div>Password:</div><div><input name="password" /></div>
            </div>
            <div>
                <div>Retype password:</div><div><input name="retyped_password" /></div>
            </div>
            <div>
                <div>Telephone:</div><div><input name="telephone" /></div>
            </div>
            <div>
                <input type="hidden" name="register" value="true" />
                <input type="submit" value="register" /> </div>
        </form>
        </jstl:if>
        <jstl:if test="${param.name != null}">
            Account already registered!
        </jstl:if>
            

