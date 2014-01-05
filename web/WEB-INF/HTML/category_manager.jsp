<%@page import="javax.persistence.Persistence"%>
<%@page import="pl.edu.pb.adserver.model.controller.CategoryJpaController"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="pl.edu.pb.adserver.model.util.CategoryBuilder" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<jstl:if test="${logged.credencials != 'ADM'}">
    <%
        String name = request.getParameter("name");
        String base = request.getParameter("base");
        CategoryJpaController cjc = 
                new CategoryJpaController(Persistence.createEntityManagerFactory("AdServerPU"));
        cjc.attachCategory(base, name);
    %>
</jstl:if>
