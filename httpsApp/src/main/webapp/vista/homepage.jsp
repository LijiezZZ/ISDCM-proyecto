<%-- 
    Document   : homepage
    Created on : 24 may 2025, 20:19:45
    Author     : alumne
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="java.util.List" %>

<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    //if (request.getAttribute("videos") == null) {
        //response.sendRedirect(request.getContextPath() + "/servletListadoVid");
       // return;
   // }
    String usuario = null;
    Usuario user = (Usuario) sessionUser.getAttribute("user");
    usuario = user.getUsername();
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homepage</title>
    </head>
    <body>
        <h1>Hola usuario: <%= usuario %></h1>
    </body>
</html>
