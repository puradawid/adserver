<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    //cookie handling
    Cookie[] cookies = request.getCookies();
    Cookie layout = null;
    if(cookies != null)
        for(Cookie cookie : cookies)
            if(cookie.getName().equals("layout")) layout = cookie;
    if (layout == null)
    {
        layout = new Cookie("layout", "default");
        response.addCookie(layout);  
    }
    String changeLayout = request.getParameter("changeLayout");
    if(changeLayout != null)
    {
        if(layout.getValue().equals("default"))
            layout.setValue("other");
        else
            layout.setValue("default");
        response.addCookie(layout);
    }
    
    request.setAttribute("layout", layout);
        
%>

<html>
    <header>
        <title>AdServer Web Pages ${cookie.dupa}</title>
        <link rel="stylesheet" type="text/css" href="<jstl:out value="${pageContext.request.servletContext.contextPath}" />/style/${layout.value}.css" />
    </header>
    
    <body>
        
        <div class="container">
            <div class="header">Ad Server</div>
            <div class="menu">
                <jstl:if test="${sessionScope.user.credencials == 'ADM' }">
                    <div class="menu_element">
                        <a href="user">Manage users</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user.credencials == 'ADM'}">
                    <div class="menu_element">
                        <a href="ad">Manage ads</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user.credencials == 'CLI'}">
                    <div class="menu_element">
                        <a href="ad">Your ads</a>
                    </div>
                </jstl:if>
                <jstl:if test="${sessionScope.user.credencials == 'PAR'}">
                    <div class="menu_element">
                        <a href="ad">Get ad</a>
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
                <div class="menu_element">
                    <a href="?changeLayout">Switch layout</a>
                </div>
            </div>
            <div class="content">