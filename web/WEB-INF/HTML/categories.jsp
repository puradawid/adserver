<%@page import="pl.edu.pb.adserver.model.Category"%>
<%@page import="pl.edu.pb.adserver.model.util.CategoryBuilder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<jstl:forEach items="${categories}" var="category">
    <option<jstl:if test = "${selected == category.name}"> selected="selected" </jstl:if>>${category.name}</option>
</jstl:forEach>

