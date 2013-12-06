<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
            <jstl:if test="${sessionScope.user != null}">
                You are logged in as <jstl:out value = "${sessionScope.user.email}" />
            </jstl:if>
            <jstl:if test="${sessionScope.user == null}">
                <%@include file="login.jsp" %>
                <%@include file="register.jsp" %>
            </jstl:if>
