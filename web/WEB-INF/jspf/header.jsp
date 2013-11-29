<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <header>
        <title>AdServer Web Pages</title>
        <link rel="stylesheet" type="text/css" href="<jstl:out value="${pageContext.request.servletContext.contextPath}" />/style/default.css" />
    </header>
    
    <body>
        
        <div class="container">
            <div class="header">Ad Server</div>
            <div class="menu">
                <jstl:if test="${sessionScope.user.credencials == 'ADM' }">
                    <div class="menu_element">
                        <a href="users">Manage users</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user.credencials == 'CLI'}">
                    <div class="menu_element">
                        <a href="ads">Your ads</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user == null}">
                    <div class="menu_element">
                        <a href="login">Login</a>
                    </div>
                    <div class="menu_element">
                        <a href="register">Register</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user != null}">
                    <div class="menu_element">
                        <a href="logout">Logout</a>
                    </div>
                    <div class="menu_element">
                        Hello <jstl:out value="${sessionScope.user.first_name}" />!
                    </div>
                </jstl:if>
            </div>
            <div class="content">